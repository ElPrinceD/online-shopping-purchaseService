package com.cradle.onlineshoppingpurchaseService.v1.services;

import com.cradle.onlineshoppingpurchaseService.v1.entities.Category;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class  CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        Objects.requireNonNull(categoryRepository, "category repository is required");
    }

    public Category save(Category category, String requestId) {
        log.info("[" + requestId + "] is about to process request to add category with name" + category.getName());
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            log.warn("an error occurred while persisting category. message: " + e.getMessage());

        }
        return category;
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
}



