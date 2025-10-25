package com.ofss.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ofss.util.CustomerIdGenerator;
@Entity
@Table(name = "customers2")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {
    @Id
    @Column(name = "CUSTOMER_ID")
    private String customerId;
    @NotBlank(message = "Full name is required")
    @Column(name = "FULL_NAME", nullable = false)
    private String fullName;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    @NotBlank(message = "Phone is required")
    @Column(name = "PHONE", nullable = false)
    private String phone;
    @NotNull(message = "Date of birth is required")
    @Column(name = "DOB", nullable = false)
    private LocalDate dob;
    @NotBlank(message = "Address is required")
    @Column(name = "ADDRESS", nullable = false)
    private String address;
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN format")
    @NotBlank(message = "PAN is required")
    @Column(name = "PAN", nullable = false, unique = true)
    private String pan;
    @Pattern(regexp = "\\d{12}", message = "AADHAAR must be 12 digits")
    @NotBlank(message = "AADHAAR is required")
    @Column(name = "AADHAAR", nullable = false, unique = true)
    private String aadhaar;
    @Column(name = "CREATED_AT")
    private LocalDate createdAt = LocalDate.now();
    @PrePersist
    public void generateCustomerId() {
        // Generate unique 4-digit number
        this.customerId = CustomerIdGenerator.generateUniqueId();
    }
    // ===== Getters and Setters =====
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
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
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}