package com.example.bookportal.service;

import com.example.bookportal.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    Long getCategoryBookCount(Long id);
}
