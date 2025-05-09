package com.onlinecourse.backend.controller;

import com.onlinecourse.backend.model.Category;
import com.onlinecourse.backend.repository.CategoryRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin  //Cho phép frontend gọi API
public class CategoryContr {
    private final CategoryRepository categoryRepository;

    public CategoryContr(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
