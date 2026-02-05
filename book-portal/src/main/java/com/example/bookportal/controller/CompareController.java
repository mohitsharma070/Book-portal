package com.example.bookportal.controller;

import com.example.bookportal.entity.Book;
import com.example.bookportal.service.BookService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/compare")
public class CompareController {

    private final BookService bookService;

    public CompareController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String comparePage(
            @RequestParam(required = false, name = "bookIds") List<Long> bookIds,
            Model model,
            Authentication auth) {
        List<Book> booksToCompare = new ArrayList<>();
        if (bookIds != null && !bookIds.isEmpty()) {
            for (Long id : bookIds) {
                try {
                    booksToCompare.add(bookService.getBookById(id));
                } catch (RuntimeException e) {
                    // Skip books that don't exist
                }
            }
        }
        model.addAttribute("books", booksToCompare);
        model.addAttribute("username", auth.getName());
        return "compare";
    }
}
