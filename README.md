# 🏦 Banking Fundamentals

**Banking Fundamentals** is a microservices-based application built using **Spring Boot**, **Spring Cloud Eureka**, and **Oracle 11g**.  
The system is designed to manage core banking functionalities such as **customer management**, **KYC document verification**, and **account handling**.

---

## 🧩 Project Architecture

This project consists of **three microservices** and a **Registry Server (Eureka)**:

1. **CustomerMS** – Manages customer information and registration.  
2. **KYCDocumentMS** – Handles KYC (Know Your Customer) document uploads, verification, and status management.  
3. **AccountMS** – Manages customer bank accounts, including creation, updates, and status tracking.  
4. **Registry Server** – Eureka service registry for service discovery.

---

## ⚙️ Technologies Used

- **Java (Spring Boot)**  
- **Spring Cloud Netflix Eureka**  
- **Oracle 11g Database**  
- **Swagger UI** for API documentation  
- **Maven** for build and dependency management  
- **STS (Spring Tool Suite)** as IDE

---

## 🗺️ System Architecture Diagram

```mermaid
flowchart TD
    subgraph Registry["Eureka Registry Server (Port: 4567)"]
    end

    subgraph Customer["CustomerMS (8085)"]
        A1[Customers2 Table]
    end

    subgraph KYC["KYCDocumentMS (8083)"]
        A2[KycDocuments2 Table]
    end

    subgraph Account["AccountMS (8084)"]
        A3[Accounts2 Table]
    end

    Client[Frontend / API Client]
    DB[(Oracle 11g Database)]

    Client -->|REST API Calls| Customer
    Client -->|REST API Calls| KYC
    Client -->|REST API Calls| Account

    Customer --> Registry
    KYC --> Registry
    Account --> Registry

    Customer --> DB
    KYC --> DB
    Account --> DB
