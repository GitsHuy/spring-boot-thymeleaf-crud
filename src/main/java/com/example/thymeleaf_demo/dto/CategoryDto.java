package com.example.thymeleaf_demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long categoryId;

    @NotEmpty(message = "Tên danh mục không được để trống")
    @Length(min = 3, message = "Tên danh mục phải có ít nhất 3 ký tự")
    private String name;

    private Boolean isEdit = false;
}

