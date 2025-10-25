package com.ofss.dto;

import org.springframework.web.multipart.MultipartFile;

public class KycUploadDTO {
    private String customerId;
    private MultipartFile pan;
    private MultipartFile aadhaar;
    private MultipartFile photo;

    // Getters and Setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public MultipartFile getPan() { return pan; }
    public void setPan(MultipartFile pan) { this.pan = pan; }

    public MultipartFile getAadhaar() { return aadhaar; }
    public void setAadhaar(MultipartFile aadhaar) { this.aadhaar = aadhaar; }

    public MultipartFile getPhoto() { return photo; }
    public void setPhoto(MultipartFile photo) { this.photo = photo; }
}
