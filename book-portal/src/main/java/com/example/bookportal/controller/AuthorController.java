package com.example.bookportal.controller;

import com.example.bookportal.service.AuthorService;
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
@RequestMapping("/authors")
@Validated
public class AuthorController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // AUTHOR SEARCH PAGE
    @GetMapping
    public String authorPage(Model model, Authentication auth) {

        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("username", auth.getName());

        logger.info("Author page accessed by user: {}", auth.getName());

        return "author";
    }

    // AUTHOR SUMMARY PAGE
    @GetMapping("/{id}")
    public String authorSummary(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorService.getAuthorById(id));
        model.addAttribute("bookCount", authorService.getAuthorBookCount(id));
        model.addAttribute("books", authorService.getBooksByAuthor(id));
        model.addAttribute("categories", authorService.getCategoryWiseBooks(id));

        logger.info("Author summary viewed for author id: {}", id);

        return "author-summary";
    }
}
