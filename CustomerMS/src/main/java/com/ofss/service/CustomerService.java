package com.ofss.service;
import com.ofss.dto.CustomerRegistrationRequest;
import com.ofss.model.Customer;
import com.ofss.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public Customer registerCustomer(CustomerRegistrationRequest request) {
        // Check duplicates
        customerRepository.findByPan(request.getPan()).ifPresent(c -> {
            throw new IllegalArgumentException("PAN already exists");
        });
        customerRepository.findByAadhaar(request.getAadhaar()).ifPresent(c -> {
            throw new IllegalArgumentException("AADHAAR already exists");
        });
        customerRepository.findByEmail(request.getEmail()).ifPresent(c -> {
            throw new IllegalArgumentException("Email already exists");
        });
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setDob(request.getDob());
        customer.setAddress(request.getAddress());
        customer.setPan(request.getPan());
        customer.setAadhaar(request.getAadhaar());
        return customerRepository.save(customer);
    }
    public Customer getCustomer(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    // :white_check_mark: DELETE
    public void deleteCustomer(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new EntityNotFoundException("Customer not found");
        }
        customerRepository.deleteById(customerId);
    }
    // :white_check_mark: PUT — replace all details
    public Customer updateCustomer(String customerId, CustomerRegistrationRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setDob(request.getDob());
        customer.setAddress(request.getAddress());
        customer.setPan(request.getPan());
        customer.setAadhaar(request.getAadhaar());
        return customerRepository.save(customer);
    }
    // :white_check_mark: PATCH — update only provided fields
    public Customer patchCustomer(String customerId, Map<String, Object> updates) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        updates.forEach((key, value) -> {
            switch (key) {
                case "fullName" -> customer.setFullName((String) value);
                case "email" -> customer.setEmail((String) value);
                case "phone" -> customer.setPhone((String) value);
                case "dob" -> customer.setDob(LocalDate.parse(value.toString()));
                case "address" -> customer.setAddress((String) value);
                case "pan" -> customer.setPan((String) value);
                case "aadhaar" -> customer.setAadhaar((String) value);
                default -> throw new IllegalArgumentException("Invalid field: " + key);
            }
        });
        return customerRepository.save(customer);
    }
}