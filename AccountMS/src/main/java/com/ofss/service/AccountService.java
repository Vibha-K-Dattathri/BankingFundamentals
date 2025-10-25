package com.ofss.service;

import com.ofss.model.Account;
import com.ofss.repository.AccountRepository;
import com.ofss.dto.AccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RestTemplate restTemplate;

    private final String kycMsUrl = "http://localhost:8083/api/kyc/status/";

    // ------------------- CREATE ACCOUNT -------------------
    public Account createAccount(String customerId, String accountType, Double initialBalance) {
        String kycStatus = getKycStatus(customerId);
        if (!"VERIFIED".equals(kycStatus)) {
            throw new RuntimeException("KYC not verified. Cannot create account.");
        }

        Optional<Account> existing = accountRepository.findByCustomerIdAndAccountType(customerId, accountType);
        if (existing.isPresent()) {
            throw new RuntimeException("Customer already has an account of type " + accountType);
        }

        Account account = new Account();
        account.setAccountId(generateUniqueAccountId());
        account.setCustomerId(customerId);
        account.setAccountType(accountType);
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setAccountBalance(initialBalance != null ? initialBalance : 0.0);
        account.setAccountStatus("ACTIVE");

        accountRepository.save(account);

        // Send email
        String customerEmail = getCustomerEmail(customerId);
        if (customerEmail != null) {
            emailService.sendEmail(customerEmail,
                    "Account Created",
                    "Your " + accountType + " account has been successfully created. Account Number: "
                            + account.getAccountNumber() + " with initial balance: " + account.getAccountBalance());
        }

        return account;
    }

    // ------------------- GET ACCOUNTS -------------------
    public List<Account> getAccountsByCustomer(String customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public Account getAccountById(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
    }
    
    // Get all accounts (for admin or general listing)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }


    // ------------------- DELETE ACCOUNT -------------------
    public void deleteAccount(String accountId) {
        Account account = getAccountById(accountId);
        accountRepository.delete(account);
    }

    // ------------------- UPDATE ACCOUNT (PUT) -------------------
    public Account updateAccount(String accountId, AccountRequest request) {
        Account account = getAccountById(accountId);

        account.setAccountType(request.getAccountType() != null ? request.getAccountType() : account.getAccountType());
        account.setAccountBalance(request.getAccountBalance() != null ? request.getAccountBalance() : account.getAccountBalance());
        account.setAccountStatus(request.getAccountStatus() != null ? request.getAccountStatus() : account.getAccountStatus());

        return accountRepository.save(account);
    }

    // ------------------- PATCH ACCOUNT -------------------
    public Account patchAccount(String accountId, Map<String, Object> updates) {
        Account account = getAccountById(accountId);

        if (updates.containsKey("accountType"))
            account.setAccountType((String) updates.get("accountType"));
        if (updates.containsKey("accountBalance"))
            account.setAccountBalance(Double.valueOf(updates.get("accountBalance").toString()));
        if (updates.containsKey("accountStatus"))
            account.setAccountStatus((String) updates.get("accountStatus"));

        return accountRepository.save(account);
    }

    // ------------------- HELPER METHODS -------------------
    private String getKycStatus(String customerId) {
        try {
            return restTemplate.getForObject(kycMsUrl + customerId, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCustomerEmail(String customerId) {
        try {
            Map<String, Object> customerData = restTemplate.getForObject(
                    "http://localhost:8085/api/customers/" + customerId, Map.class);
            if (customerData != null) return (String) customerData.get("email");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String generateUniqueAccountNumber() {
        while (true) {
            String accountNumber = String.format("%012d", (long) (Math.random() * 1_000_000_000_000L));
            boolean exists = accountRepository.findAll().stream()
                    .anyMatch(acc -> acc.getAccountNumber().equals(accountNumber));
            if (!exists) return accountNumber;
        }
    }

    private String generateUniqueAccountId() {
        while (true) {
            String accountId = String.format("%010d", (long) (Math.random() * 10_000_000_000L));
            boolean exists = accountRepository.findAll().stream()
                    .anyMatch(acc -> acc.getAccountId().equals(accountId));
            if (!exists) return accountId;
        }
    }
}
