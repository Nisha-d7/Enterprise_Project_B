package com.accountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.accountservice.model.Account;
import com.accountservice.repo.AccountRepository;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")

public class AccountController {
	
	 @Autowired private AccountRepository repo;

	  @PostMapping("/create")
	  public ResponseEntity<?> create(@Valid @RequestBody Account account) {
	    // if username exists, update or return error per preference
	    Optional<Account> exists = repo.findByUsername(account.getUsername());
	    if (exists.isPresent()) return ResponseEntity.badRequest().body(Map.of("message","username exists"));
	    Account saved = repo.save(account);
	    return ResponseEntity.ok(saved);
	  }

	  @GetMapping("/{username}")
	  public ResponseEntity<?> get(@PathVariable String username) {
	    return repo.findByUsername(username).map(ResponseEntity::ok)
	      .orElseGet(() -> ResponseEntity.notFound().build());
	  }

	  public static record DebitRequest(String username, double amount) {}
	  public static record CreditRequest(String username, double amount) {}

	  @PostMapping("/debit")
	  public ResponseEntity<?> debit(@RequestBody DebitRequest req) {
	    var op = repo.findByUsername(req.username());
	    if (op.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message","account not found"));
	    var acc = op.get();
	    synchronized (acc) {
	      if (acc.getBalance() < req.amount()) return ResponseEntity.badRequest().body(Map.of("message","insufficient funds"));
	      acc.setBalance(acc.getBalance() - req.amount());
	      repo.save(acc);
	      return ResponseEntity.ok(Map.of("message","debited","balance", acc.getBalance()));
	    }
	  }

	  @PostMapping("/credit")
	  public ResponseEntity<?> credit(@RequestBody CreditRequest req) {
	    var op = repo.findByUsername(req.username());
	    if (op.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message","account not found"));
	    var acc = op.get();
	    acc.setBalance(acc.getBalance() + req.amount());
	    repo.save(acc);
	    return ResponseEntity.ok(Map.of("message","credited","balance", acc.getBalance()));
	  }

}
