package com.cradle.onlineshoppingpurchaseService.v1.entities;

import lombok.Getter;
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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany
    private List<Product> products = new ArrayList<>();

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
