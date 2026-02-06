package com.example.bookportal.controller;

import com.example.bookportal.service.CategoryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/categories")
@Validated
public class CategoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String categoryPage(Model model, Authentication auth) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("username", auth.getName());
        logger.info("Fetched all categories for user: {}", auth.getName());
        return "category";
    }

    @GetMapping("/{id}")
    public String categorySummary(@PathVariable Long id, Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sort,
                                 @RequestParam(defaultValue = "ASC") String direction) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        model.addAttribute("bookCount", categoryService.getCategoryBookCount(id));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        Page<com.example.bookportal.entity.Book> booksPage = categoryService.getBooksByCategory(id, pageable);
        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("page", booksPage);
        logger.info("Fetched details for category ID: {}", id);
        return "category-summary";
    }
}
