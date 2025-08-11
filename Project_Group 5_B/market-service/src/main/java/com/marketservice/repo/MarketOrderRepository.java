package com.marketservice.repo;

import com.marketservice.model.MarketOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MarketOrderRepository extends MongoRepository<MarketOrder, String>  {

}
