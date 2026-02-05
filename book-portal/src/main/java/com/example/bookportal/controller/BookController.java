package com.example.bookportal.controller;

import com.example.bookportal.dto.SearchRequest;
import com.example.bookportal.dto.SearchResult;
import com.example.bookportal.service.SearchService;
import com.example.bookportal.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/books")
public class BookController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;
    private final SearchService searchService;

    public BookController(BookService bookService, SearchService searchService) {
        this.bookService = bookService;
        this.searchService = searchService;
    }

    // MVC endpoint for Thymeleaf UI (unchanged)
    @GetMapping
    public String booksPage(
            @RequestParam(required = false, name = "authorId") Long authorId,
            @RequestParam(required = false, name = "publisherId") Long publisherId,
            @RequestParam(required = false, name = "categoryId") Long categoryId,
            Model model,
            @PageableDefault(size = 10) Pageable pageable) {
        try {
            Page<?> booksPage;
            if (authorId != null && categoryId != null) {
                booksPage = bookService.getBooksByAuthorAndCategory(authorId, categoryId, pageable);
            } else if (publisherId != null && categoryId != null) {
                booksPage = bookService.getBooksByPublisherAndCategory(publisherId, categoryId, pageable);
            } else if (authorId != null) {
                booksPage = bookService.getBooksByAuthor(authorId, pageable);
            } else if (publisherId != null) {
                booksPage = bookService.getBooksByPublisher(publisherId, pageable);
            } else if (categoryId != null) {
                booksPage = bookService.getBooksByCategory(categoryId, pageable);
            } else {
                booksPage = Page.empty(pageable);
            }
            model.addAttribute("books", booksPage.getContent());
            model.addAttribute("page", booksPage);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            logger.error("Error fetching books", ex);
        }
        return "books";
    }

    // Generic REST search endpoint (production-ready, unified)
    @PostMapping("/search")
    @ResponseBody
    public ResponseEntity<?> searchBooks(@Valid @RequestBody SearchRequest request) {
        try {
            SearchResult result = searchService.search(request);
            return ok(result);
        } catch (Exception ex) {
            logger.error("Error searching books", ex);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
