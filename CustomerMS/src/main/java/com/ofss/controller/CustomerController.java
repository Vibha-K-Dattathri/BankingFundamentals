package com.ofss.controller;
import com.ofss.dto.CustomerRegistrationRequest;
import com.ofss.model.Customer;
import com.ofss.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "APIs for managing Customer Details")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    // Register customer
    @PostMapping("/register")
    @Operation(summary = "Register a new customer")
    public ResponseEntity<Customer> registerCustomer(
            @Valid @RequestBody CustomerRegistrationRequest request) {
        return ResponseEntity.ok(customerService.registerCustomer(request));
    }
    // Get customer by ID
    @GetMapping("/{customerId}")
    @Operation(summary = "Get details of a specific customer by their Customer ID")
    public ResponseEntity<Customer> getCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }
    // Get all customers
    @GetMapping("/all")
    @Operation(summary = "Get all customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
    // :white_check_mark: DELETE customer by ID
    @DeleteMapping("/{customerId}")
    @Operation(summary = "Delete a customer by Customer ID")
    public ResponseEntity<String> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok("Customer deleted successfully: " + customerId);
    }
    // :white_check_mark: PUT (replace all fields)
    @PutMapping("/{customerId}")
    @Operation(summary = "Update all details of an existing customer")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable String customerId,
            @Valid @RequestBody CustomerRegistrationRequest request) {
        Customer updated = customerService.updateCustomer(customerId, request);
        return ResponseEntity.ok(updated);
    }
    // :white_check_mark: PATCH (partial update)
    @PatchMapping("/{customerId}")
    @Operation(summary = "Partially update specific fields of a customer’s information")
    public ResponseEntity<Customer> patchCustomer(
            @PathVariable String customerId,
            @RequestBody Map<String, Object> updates) {
        Customer patched = customerService.patchCustomer(customerId, updates);
        return ResponseEntity.ok(patched);
    }
}