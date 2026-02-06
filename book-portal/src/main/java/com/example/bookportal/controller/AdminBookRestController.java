package com.example.bookportal.controller;

import com.example.bookportal.dto.BookDto;
import com.example.bookportal.dto.response.ApiResponse;
import com.example.bookportal.service.AdminBookService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RestController
@RequestMapping("/admin/books")
public class AdminBookRestController extends BaseController {
    private final AdminBookService adminBookService;

    public AdminBookRestController(AdminBookService adminBookService) {
        this.adminBookService = adminBookService;
    }

        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/all")
        public ResponseEntity<ApiResponse<Page<BookDto>>> getAllBooks(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "id") String sort,
                @RequestParam(defaultValue = "ASC") String direction) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
            Page<BookDto> books = adminBookService.getAllBooks(pageable);
            return ok(books);
        }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BookDto>>> searchBooks(
            @RequestParam("q") String query,
            @RequestParam("type") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        Page<BookDto> books;
        String normalized = type == null ? "all" : type.trim();
        switch (normalized.toLowerCase()) {
            case "author" -> books = adminBookService.searchBooksByAuthor(query, pageable);
            case "publisher" -> books = adminBookService.searchBooksByPublisher(query, pageable);
            case "category" -> books = adminBookService.searchBooksByCategory(query, pageable);
            default -> books = adminBookService.searchBooks(query, pageable);
        }
        return ok(books);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<BookDto>> createBook(@Valid @RequestBody BookDto bookDto) {
        BookDto created = adminBookService.createBook(bookDto);
        return ok(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDto>> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
        BookDto updated = adminBookService.updateBook(id, bookDto);
        return ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        adminBookService.deleteBook(id);
        return ok();
    }
}
