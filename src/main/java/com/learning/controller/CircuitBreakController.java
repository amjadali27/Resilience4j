package com.learning.controller;

import com.learning.response.Department;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakController {

    Logger logger = LoggerFactory.getLogger(CircuitBreakController.class);

    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/get-department/{id}")
    @CircuitBreaker(name = "getDepartment", fallbackMethod = "getDepartmentFallback")
    public Department getDepartment(@PathVariable("id") Integer deptId){
        logger.info("Department service call initiated...");
        Department department = restTemplate.getForObject("http://localhost:8082/department/"+deptId,Department.class);
        logger.info("Department service completed successfully...");
        return department;
    }

    public Department getDepartmentFallback(Exception e){
        logger.info("Department Service is temporarily down.Please try again...");
        return new Department(1,"Dummy Department","Fake Location");
    }
}
