package com.ofss.controller;

import com.ofss.dto.KycUploadDTO;
import com.ofss.dto.KycVerificationDTO;
import com.ofss.model.KYCDocument;
import com.ofss.service.KycService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/kyc")
@Tag(name = "Kyc Management", description = "APIs for managing Kyc Documents")
public class KycController {

    @Autowired
    private KycService kycService;

    // ---------------- Upload All Documents ----------------
    @PostMapping(value = "/upload-all", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload PAN, Aadhaar, and Photo for a customer")
    public ResponseEntity<List<KYCDocument>> uploadAll(
            @Parameter(description = "Customer ID") @RequestParam String customerId,
            @Parameter(description = "PAN Document") @RequestParam MultipartFile pan,
            @Parameter(description = "Aadhaar Document") @RequestParam MultipartFile aadhaar,
            @Parameter(description = "Photo") @RequestParam MultipartFile photo
    ) throws IOException {
        KycUploadDTO dto = new KycUploadDTO();
        dto.setCustomerId(customerId);
        dto.setPan(pan);
        dto.setAadhaar(aadhaar);
        dto.setPhoto(photo);
        return ResponseEntity.ok(kycService.uploadDocuments(dto));
    }

    // ---------------- Verify Document ----------------
    @PostMapping("/verify/{docId}")
    @Operation(summary = "Verify (approve/reject) a specific KYC document")
    public ResponseEntity<KYCDocument> verify(
            @Parameter(description = "Document ID") @PathVariable String docId,
            @Parameter(description = "Verification details") @RequestBody KycVerificationDTO dto
    ) {
        return ResponseEntity.ok(kycService.verifyDocument(docId, dto));
    }

    // ---------------- Pending Documents ----------------
    @GetMapping("/pending")
    @Operation(summary = "Get all pending KYC documents")
    public ResponseEntity<List<KYCDocument>> pendingDocuments() {
        return ResponseEntity.ok(kycService.getPendingDocuments());
    }

    // ---------------- Overall Status ----------------
    @GetMapping("/status/{customerId}")
    @Operation(summary = "Get overall KYC status for a customer")
    public ResponseEntity<String> overallStatus(
            @Parameter(description = "Customer ID") @PathVariable String customerId
    ) {
        return ResponseEntity.ok(kycService.getOverallStatus(customerId));
    }

    // ---------------- Reupload Document ----------------
    @PostMapping(value = "/reupload/{docId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Reupload a rejected KYC document")
    public ResponseEntity<KYCDocument> reuploadDocument(
            @Parameter(description = "Document ID") @PathVariable String docId,
            @Parameter(description = "File to reupload") @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (file.isEmpty()) return ResponseEntity.badRequest().body(null);
        return ResponseEntity.ok(kycService.reuploadDocument(docId, file));
    }

    // ---------------- View All Documents (Customer) ----------------
    @GetMapping("/view/customer/{customerId}")
    @Operation(summary = "View all documents uploaded by a customer (excluding file content)")
    public ResponseEntity<List<KYCDocument>> viewAllDocuments(
            @Parameter(description = "Customer ID") @PathVariable String customerId
    ) {
        return ResponseEntity.ok(kycService.viewDocumentsByCustomer(customerId));
    }

    // ---------------- View Specific Document ----------------
    @GetMapping("/{docId}")
    @Operation(summary = "Get details of a specific KYC document (without file content)")
    public ResponseEntity<KYCDocument> viewSingleDocument(
            @Parameter(description = "Document ID") @PathVariable String docId
    ) {
        return ResponseEntity.ok(kycService.viewDocumentById(docId));
    }

    // ---------------- Download / View File ----------------
    @GetMapping("/view/{docId}")
    @Operation(summary = "Download or view KYC document as file")
    public ResponseEntity<byte[]> viewDocument(
            @Parameter(description = "Document ID") @PathVariable String docId
    ) {
        KYCDocument doc = kycService.getDocumentById(docId);
        if (doc == null || doc.getFileBase64() == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            byte[] fileBytes = Base64.getDecoder().decode(doc.getFileBase64());
            String contentType = getMimeType(doc.getFileName());
            String dispositionType = contentType.startsWith("image/") ? "inline" : "attachment";
            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .header("Content-Disposition", dispositionType + "; filename=\"" + doc.getFileName() + "\"")
                    .body(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private String getMimeType(String fileName) {
        if (fileName == null) return "application/octet-stream";
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".pdf")) return "application/pdf";
        if (lower.endsWith(".txt")) return "text/plain";
        return "application/octet-stream";
    }

}
