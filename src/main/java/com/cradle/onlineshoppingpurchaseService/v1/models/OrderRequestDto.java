package com.cradle.onlineshoppingpurchaseService.v1.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderRequestDto {
    private String location;
    private List<Long> cartItemIds = new ArrayList<>();
    private String contactNumber;
    private int quantity;
}
