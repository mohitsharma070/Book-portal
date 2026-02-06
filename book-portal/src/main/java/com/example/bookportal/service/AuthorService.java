package com.example.bookportal.service;

import com.example.bookportal.entity.Author;
import com.example.bookportal.repository.projection.CategoryBookCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author getAuthorById(Long authorId);

    List<CategoryBookCountProjection> getCategoryWiseBooks(Long authorId);

    long getAuthorBookCount(Long id);

    Page<com.example.bookportal.entity.Book> getBooksByAuthor(Long authorId, Pageable pageable);
}
