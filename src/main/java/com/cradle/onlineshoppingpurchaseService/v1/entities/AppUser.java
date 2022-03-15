package com.cradle.onlineshoppingpurchaseService.v1.entities;

import com.cradle.onlineshoppingpurchaseService.v1.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@MappedSuperclass
public class AppUser implements Serializable {
    private static final long serialVersionUID = -6237626800965600662L;

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;



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
