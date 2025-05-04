package com.onlinecourse.backend.service;

import com.onlinecourse.backend.model.Category;
import com.onlinecourse.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllcateogries(){
        return categoryRepository.findAll();
    }
}
