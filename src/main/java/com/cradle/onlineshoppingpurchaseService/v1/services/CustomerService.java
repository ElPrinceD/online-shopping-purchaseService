package com.cradle.onlineshoppingpurchaseService.v1.services;

import com.cradle.onlineshoppingpurchaseService.v1.entities.Customer;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService {

   private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        Objects.requireNonNull(customerRepository, "customer repository");
    }

    public Optional<Customer> findById(Long id){
        return customerRepository.findById(id);
    }


    public Customer save(Customer customer, String requestId) {
        log.info("[" + requestId + "] is about to process request to add customer with id " + customer.getId());
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            log.warn("an error occurred while persisting customer. message: " + e.getMessage());
            throw new IllegalArgumentException(e);

        }
        return customer;
    }
}
