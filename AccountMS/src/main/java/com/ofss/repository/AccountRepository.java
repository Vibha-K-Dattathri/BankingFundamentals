package com.ofss.repository;

import com.ofss.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByCustomerIdAndAccountType(String customerId, String accountType);
    List<Account> findByCustomerId(String customerId);
}
