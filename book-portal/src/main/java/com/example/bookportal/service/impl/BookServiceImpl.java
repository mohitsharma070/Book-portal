package com.example.bookportal.service.impl;

import com.example.bookportal.entity.Book;
import com.example.bookportal.repository.BookRepository;
import com.example.bookportal.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getBooksByAuthorAndCategory(Long authorId, Long categoryId) {
        return bookRepository
                .findByAuthorIdAndCategoryId(authorId, categoryId);
    }

    @Override
    public List<Book> getBooksByPublisherAndCategory(Long publisherId, Long categoryId) {
        return bookRepository
                .findByPublisherIdAndCategoryId(publisherId, categoryId);
    }

    @Override
    public List<Book> getBooksByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }
}

