package com.cradle.onlineshoppingpurchaseService.v1.entities;

import com.cradle.onlineshoppingpurchaseService.v1.models.ProductResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToMany
    @JsonIgnore
    @JoinTable( joinColumns = @JoinColumn(name = "product_id"))
    private List<Category> categories = new ArrayList<>();

    /*@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CustomerProduct> productCustomers = new ArrayList<>();*/

    private Double price;

    private String name;

    private String imageURL;

    @CreatedBy
    @ManyToOne
    private Producer productOwner;

    @ManyToOne(targetEntity = Order.class)
    private List<Order> orders = new ArrayList<>();



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



    public ProductResponseDto toResponse(){
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setName(this.name);
        responseDto.setPrice(this.price);
        responseDto.setImageURL(this.imageURL);
        List<Long> categoryId = responseDto.getCategoryId();
        for (Category category:this.categories) {
            categoryId.add(category.getId());
        }

        responseDto.setCategoryId(categoryId);

        return responseDto;
    }
}
