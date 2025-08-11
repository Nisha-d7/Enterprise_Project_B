package com.orderservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Document(collection="orders")

public class Order {
	@Id private String id;

	  @NotBlank(message = "Username is mandatory")
	  private String username;

	  @NotBlank(message = "Symbol is mandatory")
	  private String symbol;

	  @Min(value = 1, message = "Quantity must be at least 1")
	  private int quantity;

	  private String status;
	  private Instant createdAt = Instant.now();

	  public Order() {}
	  // getters & setters...
	  public String getId(){return id;} public void setId(String id){this.id=id;}
	  public String getUsername(){return username;} public void setUsername(String u){this.username=u;}
	  public String getSymbol(){return symbol;} public void setSymbol(String s){this.symbol=s;}
	  public int getQuantity(){return quantity;} public void setQuantity(int q){this.quantity=q;}
	  public String getStatus(){return status;} public void setStatus(String s){this.status=s;}
	  public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant t){this.createdAt=t;}

}
