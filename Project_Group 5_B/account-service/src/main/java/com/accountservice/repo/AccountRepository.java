package com.accountservice.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.accountservice.model.Account;
import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {
	 Optional<Account> findByUsername(String username);
}
