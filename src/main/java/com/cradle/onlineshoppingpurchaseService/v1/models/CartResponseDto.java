package com.cradle.onlineshoppingpurchaseService.v1.models;

import com.cradle.onlineshoppingpurchaseService.v1.entities.CartItem;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CartResponseDto {
    private Long id;
    private List<CartItem> cartItems;
}
