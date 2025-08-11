package com.marketservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.marketservice.model.MarketOrder;
import com.marketservice.repo.MarketOrderRepository;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/market")
public class MarketController {
	
    @Autowired 
    private MarketOrderRepository repo;

    @Autowired 
    private RestTemplate restTemplate;

    public static record PlaceOrderReq(@NotBlank String username, @NotBlank String symbol, @Min(1) int quantity) {}

    @PostMapping("/place")
    public ResponseEntity<?> place(@Valid @RequestBody PlaceOrderReq req) {
        double price = lookupPrice(req.symbol());
        MarketOrder mo = new MarketOrder();
        mo.setUsername(req.username());
        mo.setSymbol(req.symbol().toUpperCase());
        mo.setQuantity(req.quantity());
        mo.setPrice(price);
        mo.setStatus("PENDING");
        repo.save(mo);

        String accountUrl = "http://account-service/api/accounts/debit";
        Map<String,Object> debitReq = Map.of("username", req.username(), "amount", price * req.quantity());

        try {
            ResponseEntity<Map> debitResp = restTemplate.postForEntity(accountUrl, debitReq, Map.class);
            if (debitResp.getStatusCode().is2xxSuccessful()) {
                mo.setStatus("EXECUTED");
                repo.save(mo);
                return ResponseEntity.ok(Map.of("message","executed","order", mo));
            } else {
                mo.setStatus("FAILED: INSUFFICIENT_FUNDS");
                repo.save(mo);
                return ResponseEntity.badRequest().body(Map.of("message","insufficient funds","order",mo));
            }
        } catch (Exception ex) {
            mo.setStatus("FAILED: ACCOUNT_ERROR");
            repo.save(mo);
            return ResponseEntity.internalServerError().body(Map.of("message","account error","error",ex.getMessage(),"order",mo));
        }
    }

    // GET method to fetch all market orders
    @GetMapping("/orders")
    public ResponseEntity<List<MarketOrder>> getAllOrders() {
        return ResponseEntity.ok(repo.findAll());
    }

    // GET method to fetch a specific order by ID
    @GetMapping("/orders/{id}")
    public ResponseEntity<MarketOrder> getOrderById(@PathVariable String id) {
        return repo.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private double lookupPrice(String symbol) {
        return switch (symbol.toUpperCase()) {
            case "AAPL" -> 150.0;
            case "GOOG" -> 2800.0;
            case "MSFT" -> 320.0;
            default -> 100.0;
        };
    }
}
