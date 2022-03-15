package com.cradle.onlineshoppingpurchaseService.v1.repositories;

import com.cradle.onlineshoppingpurchaseService.v1.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
