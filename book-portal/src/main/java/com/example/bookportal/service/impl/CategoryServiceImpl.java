package com.example.bookportal.service.impl;

import com.example.bookportal.entity.Book;
import com.example.bookportal.entity.Category;
import com.example.bookportal.exception.ResourceNotFoundException;
import com.example.bookportal.repository.BookRepository;
import com.example.bookportal.repository.CategoryRepository;
import com.example.bookportal.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                              BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        logger.info("Fetching all categories");
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        logger.info("Fetching category by id: {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Category not found with id: {}", id);
                    return new ResourceNotFoundException("Category not found with id: " + id);
                });
    }

    @Override
    public Long getCategoryBookCount(Long id) {
        logger.info("Fetching book count for category id: {}", id);
        return bookRepository.countByCategoryId(id);
    }

    @Override
    public Page<Book> getBooksByCategory(Long categoryId, Pageable pageable) {
        logger.info("Fetching books by category id: {} with pagination", categoryId);
        return bookRepository.findByCategory_IdAndActiveTrue(categoryId, pageable);
    }
}
