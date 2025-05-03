package com.onlinecourse.backend.controller;

import com.onlinecourse.backend.model.Category;
import com.onlinecourse.backend.service.CategoryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin  //Cô phép frontend gọi API
public class CategoryContr {
    private final CategoryService categoryService;

    public CategoryContr(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllcateogries();
    }
}
