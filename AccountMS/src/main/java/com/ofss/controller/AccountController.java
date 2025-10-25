package com.ofss.controller;

import com.ofss.model.Account;
import com.ofss.dto.AccountRequest;
import com.ofss.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "APIs for managing customer accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Create a new account for a customer")
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequest request) {
        return ResponseEntity.ok(
                accountService.createAccount(
                        request.getCustomerId(),
                        request.getAccountType(),
                        request.getAccountBalance()
                )
        );
    }

    @Operation(summary = "Get the account of a customer")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Account>> getAccounts(@PathVariable String customerId) {
        return ResponseEntity.ok(accountService.getAccountsByCustomer(customerId));
    }
    

 // Endpoint to fetch all accounts
    @Operation(summary = "Get all accounts")
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    
    @Operation(summary = "Delete an account by accountId")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok("Account deleted successfully");
    }

    @Operation(summary = "Update an account fully (PUT)")
    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountId,
                                                 @RequestBody AccountRequest request) {
        return ResponseEntity.ok(accountService.updateAccount(accountId, request));
    }

    @Operation(summary = "Update an account partially (PATCH)")
    @PatchMapping("/{accountId}")
    public ResponseEntity<Account> patchAccount(@PathVariable String accountId,
                                                @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(accountService.patchAccount(accountId, updates));
    }
}
