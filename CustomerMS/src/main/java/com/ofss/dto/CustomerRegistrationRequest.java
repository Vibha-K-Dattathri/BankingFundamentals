package com.ofss.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CustomerRegistrationRequest {

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "\\d{10}")
    @NotBlank
    private String phone;

    @NotNull
    private LocalDate dob;

    @NotBlank
    private String address;

    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}")
    @NotBlank
    private String pan;

    @Pattern(regexp = "\\d{12}")
    @NotBlank
    private String aadhaar;

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }

    public String getAadhaar() { return aadhaar; }
    public void setAadhaar(String aadhaar) { this.aadhaar = aadhaar; }
}