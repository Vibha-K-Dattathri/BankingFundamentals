package com.ofss.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SERVICE_HEALTH")
public class ServiceHealth {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "health_seq")
    @SequenceGenerator(name = "health_seq", sequenceName = "SERVICE_HEALTH_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "SERVICE_NAME", nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private String status; // UP / DOWN

    @Column(name = "CHECKED_AT", nullable = false)
    private LocalDateTime checkedAt;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCheckedAt() { return checkedAt; }
    public void setCheckedAt(LocalDateTime checkedAt) { this.checkedAt = checkedAt; }
}
