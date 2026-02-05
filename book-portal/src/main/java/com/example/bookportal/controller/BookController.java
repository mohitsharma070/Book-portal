package com.example.bookportal.controller;

import com.example.bookportal.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
        public String booksPage(
            @RequestParam(required = false, name = "authorId") Long authorId,
            @RequestParam(required = false, name = "publisherId") Long publisherId,
            @RequestParam(required = false, name = "categoryId") Long categoryId,
            Model model) {

        if (authorId != null && categoryId != null) {
            model.addAttribute("books",
                    bookService.getBooksByAuthorAndCategory(authorId, categoryId));
        } else if (publisherId != null && categoryId != null) {
            model.addAttribute("books",
                    bookService.getBooksByPublisherAndCategory(publisherId, categoryId));
        } else if (categoryId != null) {
            model.addAttribute("books",
                    bookService.getBooksByCategory(categoryId));
        }

        return "books";
    }
}

