package com.ofss.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "kyc_documents2")
public class KYCDocument {

    @Id
    @Column(name = "doc_id", length = 10)
    private String docId; // 6-digit numeric ID as String

    @Column(name = "customer_id", length = 36, nullable = false)
    private String customerId;

    @Column(name = "doc_type", length = 20, nullable = false)
    private String docType;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Lob
    @JsonIgnore
    @Column(name = "file_base64")
    private String fileBase64;

    @Column(name = "status", length = 20)
    private String status = "PENDING";

    @Column(name = "admin_comment", length = 2000)
    private String adminComment;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();

    // Getters and Setters
    public String getDocId() { return docId; }
    public void setDocId(String docId) { this.docId = docId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getDocType() { return docType; }
    public void setDocType(String docType) { this.docType = docType; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileBase64() { return fileBase64; }
    public void setFileBase64(String fileBase64) { this.fileBase64 = fileBase64; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAdminComment() { return adminComment; }
    public void setAdminComment(String adminComment) { this.adminComment = adminComment; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime date) { this.uploadedAt = date; }
}
