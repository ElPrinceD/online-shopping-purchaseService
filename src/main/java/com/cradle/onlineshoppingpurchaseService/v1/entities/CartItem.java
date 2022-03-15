package com.cradle.onlineshoppingpurchaseService.v1.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Product product;

    private Integer quantity;

    @ManyToOne
    private Order orders;

    @JoinColumn(name = "shopping_cart_ID")
    @ManyToOne
    private ShoppingCart shoppingCart;

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
}
