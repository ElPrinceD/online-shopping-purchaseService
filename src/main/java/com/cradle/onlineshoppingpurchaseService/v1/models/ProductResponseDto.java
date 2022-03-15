package com.cradle.onlineshoppingpurchaseService.v1.models;


import com.cradle.onlineshoppingpurchaseService.v1.entities.Category;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductResponseDto {
    private Double price;
    private String name;
    private String imageURL;
    private List<Long> categoryId = new ArrayList<>();
}
