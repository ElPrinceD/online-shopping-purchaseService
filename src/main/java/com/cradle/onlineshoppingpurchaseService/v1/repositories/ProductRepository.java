package com.cradle.onlineshoppingpurchaseService.v1.repositories;

import com.cradle.onlineshoppingpurchaseService.v1.entities.Category;
import com.cradle.onlineshoppingpurchaseService.v1.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategories(Category category, Pageable pageable);
    Page<Product> findAllByNameContaining(String name, Pageable pageable);
}
