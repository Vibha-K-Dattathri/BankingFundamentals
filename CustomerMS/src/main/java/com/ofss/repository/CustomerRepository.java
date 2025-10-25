package com.ofss.repository;

import com.ofss.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPan(String pan);
    Optional<Customer> findByAadhaar(String aadhaar);
}