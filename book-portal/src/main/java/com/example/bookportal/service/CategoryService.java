package com.example.bookportal.service;

import com.example.bookportal.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    Long getCategoryBookCount(Long id);

    Page<com.example.bookportal.entity.Book> getBooksByCategory(Long categoryId, Pageable pageable);
}
