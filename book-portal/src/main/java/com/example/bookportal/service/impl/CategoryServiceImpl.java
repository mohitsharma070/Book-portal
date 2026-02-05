package com.example.bookportal.service.impl;

import com.example.bookportal.entity.Category;
import com.example.bookportal.repository.BookRepository;
import com.example.bookportal.repository.CategoryRepository;
import com.example.bookportal.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                              BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + id));
    }

    @Override
    public Long getCategoryBookCount(Long id) {
        return bookRepository.countByCategoryId(id);
    }
}
