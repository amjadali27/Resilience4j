package com.learning.controller;

import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RetryController {
    Logger logger = LoggerFactory.getLogger(RetryController.class);
    RestTemplate restTemplate= new RestTemplate();

    @GetMapping("/checkMessageRetry")
    @Retry(name = "checkMessageRetry", fallbackMethod = "checkMessageRetryFallback")
    public String checkMessageRetry() {
        logger.info("Department Service Calling...");
        ResponseEntity<String> entity= restTemplate.getForEntity("http://localhost:8082/department/get-message", String.class);
        logger.info("Response :" + entity.getStatusCode());
        return entity.getBody();
    }

    public String checkMessageRetryFallback(Exception e) {
        logger.info("---RESPONSE FROM FALLBACK METHOD---");
        return "DEPARTMENT SERVICE IS DOWN, PLEASE TRY AFTER SOMETIME !!!";
    }
}
