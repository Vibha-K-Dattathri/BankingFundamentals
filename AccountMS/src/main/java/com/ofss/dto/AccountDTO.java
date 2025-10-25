package com.ofss.dto;

import java.time.LocalDateTime;

public class AccountDTO {
    private Long kycId;
    private Long customerId;
    private String status;
    private LocalDateTime verificationDate;

    // Getters & Setters
    public Long getKycId() { return kycId; }
    public void setKycId(Long kycId) { this.kycId = kycId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getVerificationDate() { return verificationDate; }
    public void setVerificationDate(LocalDateTime verificationDate) { this.verificationDate = verificationDate; }
}
