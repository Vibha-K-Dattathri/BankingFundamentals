package com.ofss.repository;

import com.ofss.model.KYCDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface KycDocumentRepository extends JpaRepository<KYCDocument, String> {

    List<KYCDocument> findByCustomerId(String customerId);

    List<KYCDocument> findByStatus(String status);

    Optional<KYCDocument> findByCustomerIdAndDocType(String customerId, String docType);
}
