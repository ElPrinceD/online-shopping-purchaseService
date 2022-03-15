package com.cradle.onlineshoppingpurchaseService.v1.entities;

import com.cradle.onlineshoppingpurchaseService.v1.models.CartResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Table(name = "shopping_cart")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long id;

    @OneToMany(mappedBy = "shoppingCart")
    private List<CartItem> cartItems = new ArrayList<>();

    @CreatedDate
    private ZonedDateTime createdOn;

    @LastModifiedDate
    private ZonedDateTime updatedOn;

    @OneToOne
    private Customer customer;

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


    public CartResponseDto toResponseDto(){
        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setCartItems(this.cartItems);
        responseDto.setId(this.id);

        return responseDto;
    }


}