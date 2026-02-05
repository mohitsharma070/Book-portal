package com.example.bookportal.controller;

import com.example.bookportal.service.CategoryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String categorySummary(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        model.addAttribute("bookCount", categoryService.getCategoryBookCount(id));
        logger.info("Fetched details for category ID: {}", id);
        return "category-summary";
    }
}
