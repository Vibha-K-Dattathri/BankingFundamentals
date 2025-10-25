package com.ofss.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account creation/update request")
public class AccountRequest {

    @Schema(description = "Customer ID", example = "8186", required = true)
    private String customerId;

    @Schema(description = "Type of account", example = "SAVINGS", required = true)
    private String accountType;

    @Schema(description = "Initial account balance", example = "1000.0")
    private Double accountBalance;

    @Schema(description = "Account status (for update/patch)", example = "ACTIVE")
    private String accountStatus;

    // Getters and setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public Double getAccountBalance() { return accountBalance; }
    public void setAccountBalance(Double accountBalance) { this.accountBalance = accountBalance; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
}
