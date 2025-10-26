package com.ofss.controller;

import com.ofss.service.HealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private HealthCheckService healthCheckService;

    // List all microservices registered in Eureka
    private final String[] services = {"CustomerMS", "KYCDocumentMS", "AccountMS"};

    @GetMapping("/health")
    public Map<String, String> getAllServiceHealth() {
        Map<String, String> healthMap = new HashMap<>();
        for (String service : services) {
            healthMap.put(service, healthCheckService.checkService(service));
        }
        return healthMap;
    }
}
