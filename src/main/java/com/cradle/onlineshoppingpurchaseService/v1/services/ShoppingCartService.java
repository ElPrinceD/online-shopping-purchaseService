package com.cradle.onlineshoppingpurchaseService.v1.services;

import com.cradle.onlineshoppingpurchaseService.v1.entities.CartItem;
import com.cradle.onlineshoppingpurchaseService.v1.entities.ShoppingCart;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.CartItemRepository;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.ShoppingCartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, CartItemRepository cartItemRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
        Objects.requireNonNull(cartItemRepository, "cart item repository is required");
        Objects.requireNonNull(shoppingCartRepository, "shopping cart repository is required");
    }

    public ShoppingCart save(ShoppingCart shoppingCart, String requestId){
        log.info("[" +requestId + "] is about to process request to add shopping cart with id" + shoppingCart.getId());
        try {
            shoppingCartRepository.save(shoppingCart);
        } catch (Exception e){
            log.warn("an error occurred while persisting shopping cart. message: " + e.getMessage());

        }
        return shoppingCart;
    }

    public CartItem saveCartItem(CartItem cartItem, String requestId){
        log.info("[" +requestId + "] is about to process request to add cart item with id" + cartItem.getId());
        try {
            cartItemRepository.save(cartItem);
        } catch (Exception e){
            log.warn("an error occurred while persisting cart item. message: " + e.getMessage());

        }
        return cartItem;
    }

    public Optional<ShoppingCart> findById(Long id){
        return shoppingCartRepository.findById(id);
    }

    public Optional<CartItem> findCartById(Long id){
        return cartItemRepository.findById(id);
    }
}
