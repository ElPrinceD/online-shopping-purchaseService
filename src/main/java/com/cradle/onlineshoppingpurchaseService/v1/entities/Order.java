package com.cradle.onlineshoppingpurchaseService.v1.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Purchase")
public class Order{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String location;

    @OneToMany(mappedBy = "orders",targetEntity = CartItem.class, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    private Double totalPrice;


    private Boolean completed = false;

    @ManyToOne
    private Customer customer;

    @CreatedDate
    private ZonedDateTime createdOn;

    @LastModifiedDate
    private ZonedDateTime updatedOn;

    @PrePersist
    public void prePersist() {

        ZonedDateTime currentTime = ZonedDateTime.now();
        setCreatedOn(currentTime);
        setUpdatedOn(currentTime);
    }

    @PreUpdate
    public void preUpdate() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        setUpdatedOn(currentTime);
    }


    String getProductName(){
        String name = null;
        for (CartItem cart: cartItems) {
            name = cart.getProduct().getName();
        }
        return name;
    }
    @Override
    public String toString() {
        return "Order{" +
                " location='" + location + '\'' +
                ", products=" + getProductName() +
                ", total Price=" + totalPrice +
                ", customer=" + customer.getFirstName() +
                '}';
    }
}
