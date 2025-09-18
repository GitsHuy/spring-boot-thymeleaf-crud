package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CategoryService {
    Category save(Category category);
    void deleteById(Long id);
    Optional<Category> findById(Long id);
    Page<Category> findAll(Pageable pageable);
    Page<Category> findByNameContaining(String name, Pageable pageable);
}

