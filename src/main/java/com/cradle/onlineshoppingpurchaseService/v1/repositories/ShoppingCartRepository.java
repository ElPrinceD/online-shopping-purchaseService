package com.cradle.onlineshoppingpurchaseService.v1.repositories;

import com.cradle.onlineshoppingpurchaseService.v1.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}
