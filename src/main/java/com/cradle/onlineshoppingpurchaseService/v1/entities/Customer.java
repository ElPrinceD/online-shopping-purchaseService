package com.cradle.onlineshoppingpurchaseService.v1.entities;

import com.cradle.onlineshoppingpurchaseService.v1.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Customer extends AppUser{



    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", orphanRemoval = true)
    @JsonIgnore
    private List<CustomerProduct> products = new ArrayList<>();*/

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    @OneToOne
    private ShoppingCart shoppingCart;


}
