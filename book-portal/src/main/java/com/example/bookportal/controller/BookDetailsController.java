package com.example.bookportal.controller;

import com.example.bookportal.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/book-details")
@Validated
public class BookDetailsController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BookDetailsController.class);

    private final BookService bookService;

    public BookDetailsController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public String bookDetails(@PathVariable Long id, Model model) {
        logger.info("Fetching details for book with id: {}", id);
        model.addAttribute("book", bookService.getBookById(id));
        return "book-details";
    }
}
