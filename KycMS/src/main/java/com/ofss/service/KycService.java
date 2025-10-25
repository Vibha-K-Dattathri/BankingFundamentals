package com.ofss.service;

import com.ofss.model.KYCDocument;
import com.ofss.repository.KycDocumentRepository;
import com.ofss.dto.KycUploadDTO;
import com.ofss.dto.KycVerificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class KycService {

    @Autowired
    private KycDocumentRepository kycRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RestTemplate restTemplate;

    private final String uploadDir = "C:/kyc_uploads/";
    private final String customerMsUrl = "http://localhost:8085/api/customers/";
    
    private String generateUniqueDocId() {
        String docId;
        Random random = new Random();
        do {
            docId = String.valueOf(100000 + random.nextInt(900000)); // 6-digit numeric
        } while (kycRepository.existsById(docId));
        return docId;
    }

    /**
     * Upload all KYC documents for a customer.
     * A customer can upload only once unless all previous docs are rejected.
     */
    public List<KYCDocument> uploadDocuments(KycUploadDTO dto) throws IOException {
        String customerId = dto.getCustomerId();

        // Check if the customer already uploaded documents
        List<KYCDocument> existingDocs = kycRepository.findByCustomerId(customerId);
        if (!existingDocs.isEmpty()) {
            boolean allRejected = existingDocs.stream().allMatch(d -> "REJECTED".equals(d.getStatus()));

            if (!allRejected) {
                throw new RuntimeException(
                        "KYC documents already uploaded. You cannot upload again unless all previous documents are rejected."
                );
            } else {
                // Clean up old rejected documents
                kycRepository.deleteAll(existingDocs);
                File customerDir = new File(uploadDir + customerId);
                if (customerDir.exists()) {
                    for (File f : Objects.requireNonNull(customerDir.listFiles())) f.delete();
                    customerDir.delete();
                }
            }
        }

        // Create a new directory for this upload
        File customerDir = new File(uploadDir + customerId);
        if (!customerDir.exists()) customerDir.mkdirs();

        // Save all documents
        KYCDocument panDoc = saveSingleDocument(customerId, "PAN", dto.getPan());
        KYCDocument aadhaarDoc = saveSingleDocument(customerId, "AADHAAR", dto.getAadhaar());
        KYCDocument photoDoc = saveSingleDocument(customerId, "PHOTO", dto.getPhoto());

        return List.of(panDoc, aadhaarDoc, photoDoc);
    }

    private KYCDocument saveSingleDocument(String customerId, String type, MultipartFile file) throws IOException {
        KYCDocument doc = new KYCDocument();
        doc.setDocId(generateUniqueDocId());
        doc.setCustomerId(customerId);
        doc.setDocType(type);
        doc.setFileName(file.getOriginalFilename());
        doc.setFileBase64(Base64.getEncoder().encodeToString(file.getBytes()));
        doc.setStatus("PENDING");
        doc.setUploadedAt(LocalDateTime.now());

        saveFileToDisk(customerId, file);
        return kycRepository.save(doc);
    }

    private void saveFileToDisk(String customerId, MultipartFile file) throws IOException {
        File dir = new File(uploadDir + customerId);
        if (!dir.exists()) dir.mkdirs();
        File savedFile = new File(dir, file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(savedFile)) {
            fos.write(file.getBytes());
        }
    }

    /**
     * Verify a document.
     * Once approved, cannot be modified again.
     * If rejected, can be approved again after reupload.
     */
    public KYCDocument verifyDocument(String docId, KycVerificationDTO dto) {
        KYCDocument doc = kycRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // Restrict re-verification of approved docs
        if ("VERIFIED".equals(doc.getStatus())) {
            throw new RuntimeException("This document is already verified and cannot be modified again.");
        }

        // Update status based on admin decision
        if (dto.isApprove()) {
            doc.setStatus("VERIFIED");
        } else {
            doc.setStatus("REJECTED");
        }

        doc.setAdminComment(dto.getComment());
        kycRepository.save(doc);

        // Fetch email from Customer Microservice
        String customerEmail = getCustomerEmail(doc.getCustomerId());
        if (customerEmail != null) {
            String subject = "KYC Document " + (dto.isApprove() ? "Approved" : "Rejected");
            String body = "Your " + doc.getDocType() + " document has been " + doc.getStatus() +
                    ".\n\nAdmin Comment: " + (dto.getComment() == null ? "No comment" : dto.getComment());
            emailService.sendEmail(customerEmail, subject, body);
        }

        return doc;
    }

    private String getCustomerEmail(String customerId) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(customerMsUrl + customerId, Map.class);
            Map<String, Object> customerData = response.getBody();
            if (customerData != null) return (String) customerData.get("email");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<KYCDocument> getPendingDocuments() {
        return kycRepository.findByStatus("PENDING");
    }

    public String getOverallStatus(String customerId) {
        List<KYCDocument> docs = kycRepository.findByCustomerId(customerId);
        if (docs.isEmpty()) return "NO_DOCUMENTS";

        boolean allVerified = docs.stream().allMatch(d -> "VERIFIED".equals(d.getStatus()));
        boolean anyRejected = docs.stream().anyMatch(d -> "REJECTED".equals(d.getStatus()));
        boolean anyPending = docs.stream().anyMatch(d -> "PENDING".equals(d.getStatus()));

        if (allVerified) return "VERIFIED";
        if (anyRejected) return "REJECTED";
        if (anyPending) return "PENDING";
        return "UNKNOWN";
    }

    public KYCDocument reuploadDocument(String docId, MultipartFile file) throws IOException {
        KYCDocument doc = kycRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!"REJECTED".equals(doc.getStatus())) {
            throw new RuntimeException("Only rejected documents can be reuploaded.");
        }

        doc.setFileName(file.getOriginalFilename());
        doc.setFileBase64(Base64.getEncoder().encodeToString(file.getBytes()));
        doc.setStatus("PENDING");
        doc.setAdminComment(null);
        saveFileToDisk(doc.getCustomerId(), file);

        // Notify customer
        String customerEmail = getCustomerEmail(doc.getCustomerId());
        if (customerEmail != null) {
            emailService.sendEmail(customerEmail,
                    "KYC Document Reuploaded: " + doc.getDocType(),
                    "You have successfully reuploaded your " + doc.getDocType() + " document. It is now pending verification.");
        }

        return kycRepository.save(doc);
    }
    
 // ---------------- View Documents ----------------
    public List<KYCDocument> viewDocumentsByCustomer(String customerId) {
        List<KYCDocument> docs = kycRepository.findByCustomerId(customerId);
        docs.forEach(d -> d.setFileBase64(null));
        return docs;
    }
    public KYCDocument viewDocumentById(String docId) {
        Optional<KYCDocument> optionalDoc = kycRepository.findById(docId);
        if (optionalDoc.isEmpty()) throw new RuntimeException("Document not found");
        KYCDocument doc = optionalDoc.get();
        doc.setFileBase64(null);
        return doc;
    }
    public KYCDocument getDocumentById(String docId) {
        return kycRepository.findById(docId).orElse(null);
    }

}
