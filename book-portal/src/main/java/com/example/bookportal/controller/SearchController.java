package com.example.bookportal.controller;

import com.example.bookportal.entity.Author;
import com.example.bookportal.entity.Book;
import com.example.bookportal.entity.Publisher;
import com.example.bookportal.repository.AuthorRepository;
import com.example.bookportal.repository.BookRepository;
import com.example.bookportal.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@RestController
public class SearchController {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    @GetMapping("/api/search")
    public List<Map<String, Object>> search(@RequestParam String type, @RequestParam String query) {
        if (type.equalsIgnoreCase("Author")) {
            return authorRepository.findByNameContainingIgnoreCase(query)
                    .stream()
                    .map(author -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", author.getName());
                        // Count books by author
                        map.put("books", bookRepository.findByAuthorId(author.getId()).size());
                        return map;
                    })
                    .collect(Collectors.toList());
        } else if (type.equalsIgnoreCase("Title")) {
            List<Book> booksByTitle = bookRepository.findByTitleContainingIgnoreCase(query);
            return booksByTitle.stream()
                    .map(book -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("title", book.getTitle());
                        map.put("author", book.getAuthor() != null ? book.getAuthor().getName() : "");
                        map.put("publisher", book.getPublisher() != null ? book.getPublisher().getPublisherName() : "");
                        map.put("category", book.getCategory() != null ? book.getCategory().getCategoryName() : "");
                        return map;
                    })
                    .collect(Collectors.toList());
        } else if (type.equalsIgnoreCase("Publisher")) {
            return publisherRepository.findByPublisherNameContainingIgnoreCase(query)
                    .stream()
                    .map(pub -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", pub.getPublisherName());
                        // Count books by publisher
                        map.put("books", bookRepository.findByPublisherId(pub.getId()).size());
                        return map;
                    })
                    .collect(Collectors.toList());
        } else if (type.equalsIgnoreCase("Category")) {
            return bookRepository.findByCategory_CategoryNameContainingIgnoreCase(query)
                    .stream()
                    .map(book -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("title", book.getTitle());
                        map.put("author", book.getAuthor() != null ? book.getAuthor().getName() : "");
                        map.put("publisher", book.getPublisher() != null ? book.getPublisher().getPublisherName() : "");
                        map.put("category", book.getCategory() != null ? book.getCategory().getCategoryName() : "");
                        return map;
                    })
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
