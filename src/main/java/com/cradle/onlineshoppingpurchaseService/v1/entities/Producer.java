package com.cradle.onlineshoppingpurchaseService.v1.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Product_Owner")
public class Producer extends AppUser{


    @OneToMany(mappedBy = "productOwner")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();


    @OneToMany
    private List<Order> orders = new ArrayList<>();





}