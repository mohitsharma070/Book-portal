package com.example.bookportal.service.impl;

import com.example.bookportal.entity.Author;
import com.example.bookportal.entity.Book;
import com.example.bookportal.exception.ValidationException;
import com.example.bookportal.repository.AuthorRepository;
import com.example.bookportal.repository.BookRepository;
import com.example.bookportal.repository.projection.CategoryBookCountProjection;
import com.example.bookportal.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository,
                             BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Author> getAllAuthors() {
        logger.info("Fetching all authors");
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(Long authorId) {
        if (authorId == null || authorId <= 0) {
            throw new ValidationException("Invalid author ID");
        }
        logger.info("Fetching author by id: {}", authorId);
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    @Override
    public List<CategoryBookCountProjection> getCategoryWiseBooks(Long authorId) {
        logger.info("Fetching category wise books for author id: {}", authorId);
        return bookRepository.findCategoryWiseBookCountByAuthor(authorId);
    }

    @Override
    public long getAuthorBookCount(Long id) {
        logger.info("Fetching book count for author id: {}", id);
        return bookRepository.countByAuthorId(id);
    }

    @Override
    public Page<Book> getBooksByAuthor(Long authorId, Pageable pageable) {
        logger.info("Fetching books by author id: {} with pagination", authorId);
        return bookRepository.findByAuthor_IdAndActiveTrue(authorId, pageable);
    }
}
