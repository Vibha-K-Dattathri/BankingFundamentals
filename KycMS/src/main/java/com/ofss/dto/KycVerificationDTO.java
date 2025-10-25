package com.ofss.dto;

public class KycVerificationDTO {
    private boolean approve;
    private String comment;
//    private String customerEmail;

    // Getters and Setters
    public boolean isApprove() { return approve; }
    public void setApprove(boolean approve) { this.approve = approve; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

//    public String getCustomerEmail() { return customerEmail; }
//    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
}
