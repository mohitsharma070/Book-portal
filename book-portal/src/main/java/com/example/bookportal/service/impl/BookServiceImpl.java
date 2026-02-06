package com.example.bookportal.service.impl;

import com.example.bookportal.entity.Book;
import com.example.bookportal.exception.ValidationException;
import com.example.bookportal.repository.BookRepository;
import com.example.bookportal.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book getBookById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid book ID");
        }
        logger.info("Fetching book by id: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    @Override
    public Page<Book> getBooksByAuthorAndCategory(Long authorId, Long categoryId, Pageable pageable) {
        logger.info("Fetching books by authorId: {}, categoryId: {} with pagination", authorId, categoryId);
        return bookRepository.findByAuthor_IdAndCategory_IdAndActiveTrue(authorId, categoryId, pageable);
    }

    @Override
    public Page<Book> getBooksByPublisherAndCategory(Long publisherId, Long categoryId, Pageable pageable) {
        logger.info("Fetching books by publisherId: {}, categoryId: {} with pagination", publisherId, categoryId);
        return bookRepository.findByPublisher_IdAndCategory_IdAndActiveTrue(publisherId, categoryId, pageable);
    }

    @Override
    public Page<Book> getBooksByCategory(Long categoryId, Pageable pageable) {
        logger.info("Fetching books by categoryId: {} with pagination", categoryId);
        return bookRepository.findByCategory_IdAndActiveTrue(categoryId, pageable);
    }

    @Override
    public Page<Book> getBooksByAuthor(Long authorId, Pageable pageable) {
        logger.info("Fetching books by authorId: {} with pagination", authorId);
        return bookRepository.findByAuthor_IdAndActiveTrue(authorId, pageable);
    }

    @Override
    public Page<Book> getBooksByPublisher(Long publisherId, Pageable pageable) {
        logger.info("Fetching books by publisherId: {} with pagination", publisherId);
        return bookRepository.findByPublisher_IdAndActiveTrue(publisherId, pageable);
    }
}
