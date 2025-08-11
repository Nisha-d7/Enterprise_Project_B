package com.marketservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@Configuration

public class RestTemplateConfig {
	
    @Bean // Creates a RestTemplate bean so it can be injected anywhere
    @LoadBalanced // Enables Eureka client-side load balancing for service calls
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
