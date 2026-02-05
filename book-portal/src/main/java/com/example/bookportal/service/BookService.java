package com.example.bookportal.service;

import com.example.bookportal.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getBooksByAuthorAndCategory(Long authorId, Long categoryId);
    List<Book> getBooksByPublisherAndCategory(Long publisherId, Long categoryId);
    List<Book> getBooksByCategory(Long categoryId);
    Book getBookById(Long id);
}
