package com.cradle.onlineshoppingpurchaseService.v1.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AddProductModel {
    private Double price;
    private String name;
    private String imageURL;
    private List<Long> categoryIds = new ArrayList<>();
}
