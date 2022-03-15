package com.cradle.onlineshoppingpurchaseService.v1.services;

import com.cradle.onlineshoppingpurchaseService.v1.entities.CartItem;
import com.cradle.onlineshoppingpurchaseService.v1.entities.Category;
import com.cradle.onlineshoppingpurchaseService.v1.entities.Product;
import com.cradle.onlineshoppingpurchaseService.v1.entities.Producer;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.CartItemRepository;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.ProductRepository;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        Objects.requireNonNull(cartItemRepository, "cart Item repository");
        Objects.requireNonNull(userRepository, "user repository is required");
        Objects.requireNonNull(productRepository, "product repository is required");
    }

    public Product save(Product product, String requestId) {
        log.info("[" + requestId + "] is about to process request to add product with name" + product.getName());
        try {
            productRepository.save(product);
        } catch (Exception e) {
            log.warn("an error occurred while persisting product. message: " + e.getMessage());

        }
        return product;
    }

    public void updateOnChildEntities(Product product) {
        if (product != null) {
            Producer owner = product.getProductOwner();
            List<Category> categoryList = product.getCategories();
            if (owner != null) {
                List<Product> products = owner.getProducts();
                products.add(product);
                owner.setProducts(products);
                try {
                    userRepository.save(owner);
                } catch (Exception e) {
                    log.warn("an error occurred while persisting user. message: " + e.getMessage());

                }
            }

            if (!categoryList.isEmpty()) {
                for (Category category : categoryList) {
                    List<Product> products = category.getProducts();
                    products.add(product);
                    category.setProducts(products);
                }
            }
        }
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    public CartItem saveCartItem(CartItem cartItem, String requestId) {
        log.info("[" + requestId + "] is about to process request to add cartItem with name" + cartItem.getId());
        try {
            cartItemRepository.save(cartItem);
        } catch (Exception e) {
            log.warn("an error occurred while persisting cart item. message: " + e.getMessage());

        }
        return cartItem;
    }

    public Page<Product> getProductByCategory(Category category, Pageable pageable) {
        return productRepository.findAllByCategories(category, pageable);
    }


    public Page<Product> findByName(String productName, Pageable pageable) {
        return productRepository.findAllByNameContaining(productName, pageable);
    }
}
