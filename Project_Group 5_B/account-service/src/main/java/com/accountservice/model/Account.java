package com.accountservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Document(collection = "accounts")
public class Account {
	
	@Id
	  private String id;

	  @NotBlank(message = "Username is mandatory")
	  private String username;

	  @Min(value = 0, message = "Balance must be >= 0")
	  private double balance;

	  public Account() {}
	  public Account(String username, double balance) { this.username = username; this.balance = balance; }
	  // getters & setters
	  public String getId(){return id;}
	  public void setId(String id){this.id=id;}
	  public String getUsername(){return username;}
	  public void setUsername(String username){this.username=username;}
	  public double getBalance(){return balance;}
	  public void setBalance(double balance){this.balance=balance;}

}
