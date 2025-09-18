package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Spring Data JPA tự động tạo câu lệnh query cho phương thức này
    Page<Category> findByNameContaining(String name, Pageable pageable);
}

