package com.ofss.repository;

import com.ofss.model.ServiceHealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceHealthRepository extends JpaRepository<ServiceHealth, Long> {
}
