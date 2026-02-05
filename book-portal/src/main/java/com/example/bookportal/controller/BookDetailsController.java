package com.example.bookportal.controller;

import com.example.bookportal.service.BookService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/book-details")
public class BookDetailsController {

    private final BookService bookService;

    public BookDetailsController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public String bookDetails(@PathVariable Long id, Model model, Authentication auth) {
        model.addAttribute("book", bookService.getBookById(id));
        model.addAttribute("username", auth.getName());
        return "book-details";
    }
}
