package com.cradle.onlineshoppingpurchaseService.v1.models;

import com.cradle.onlineshoppingpurchaseService.v1.entities.Customer;
import com.cradle.onlineshoppingpurchaseService.v1.entities.Producer;
import com.cradle.onlineshoppingpurchaseService.v1.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.time.ZonedDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Role role;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;

    public Customer toCustomer(){
        Customer customer = new Customer();
        customer.setId(this.id);
        customer.setUsername(this.username);
        customer.setFirstName(this.firstName);
        customer.setLastName(this.lastName);
        customer.setRole(this.role);
        customer.setCreatedOn(this.createdOn);
        customer.setUpdatedOn(this.updatedOn);

        return customer;
    }

    public Producer toProducer(){
        Producer producer = new Producer();
        producer.setId(this.id);
        producer.setUsername(this.username);
        producer.setFirstName(this.firstName);
        producer.setLastName(this.lastName);
        producer.setRole(this.role);
        producer.setCreatedOn(this.createdOn);
        producer.setUpdatedOn(this.updatedOn);

        return producer;
    }
}