package com.example.bookportal.service;

import com.example.bookportal.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    List<Book> getBooksByAuthorAndCategory(Long authorId, Long categoryId);
    List<Book> getBooksByPublisherAndCategory(Long publisherId, Long categoryId);
    List<Book> getBooksByCategory(Long categoryId);
    Book getBookById(Long id);

    Page<Book> getBooksByAuthorAndCategory(Long authorId, Long categoryId, Pageable pageable);
    Page<Book> getBooksByPublisherAndCategory(Long publisherId, Long categoryId, Pageable pageable);
    Page<Book> getBooksByCategory(Long categoryId, Pageable pageable);
    Page<Book> getBooksByAuthor(Long authorId, Pageable pageable);
    Page<Book> getBooksByPublisher(Long publisherId, Pageable pageable);
}
