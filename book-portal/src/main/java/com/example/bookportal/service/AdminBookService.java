package com.example.bookportal.service;

import com.example.bookportal.dto.BookDto;
import com.example.bookportal.entity.Book;
import com.example.bookportal.entity.Author;
import com.example.bookportal.entity.Category;
import com.example.bookportal.entity.Publisher;
import com.example.bookportal.repository.BookRepository;
import com.example.bookportal.repository.AuthorRepository;
import com.example.bookportal.repository.CategoryRepository;
import com.example.bookportal.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminBookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;

    public AdminBookService(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           CategoryRepository categoryRepository,
                           PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
    }

    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAllBookDtos(pageable);
    }
    public Page<BookDto> searchBooks(String query, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(query, pageable)
                .map(this::toDto);
    }
    public Page<BookDto> searchBooksByAuthor(String query, Pageable pageable) {
        return bookRepository.findByAuthorNameContainingIgnoreCase(query, pageable)
                .map(this::toDto);
    }
    public Page<BookDto> searchBooksByPublisher(String query, Pageable pageable) {
        return bookRepository.findByPublisherNameContainingIgnoreCase(query, pageable)
                .map(this::toDto);
    }
    public Page<BookDto> searchBooksByCategory(String query, Pageable pageable) {
        return bookRepository.findByCategoryNameContainingIgnoreCase(query, pageable)
                .map(this::toDto);
    }

    public BookDto createBook(BookDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setPrice(dto.getPrice());
        book.setImageUrl(dto.getImageUrl());
        book.setAuthor(authorRepository.findById(dto.getAuthorId()).orElseThrow());
        book.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow());
        book.setPublisher(publisherRepository.findById(dto.getPublisherId()).orElseThrow());
        return toDto(bookRepository.save(book));
    }

    public BookDto updateBook(Long id, BookDto dto) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setTitle(dto.getTitle());
        book.setPrice(dto.getPrice());
        book.setImageUrl(dto.getImageUrl());
        book.setAuthor(authorRepository.findById(dto.getAuthorId()).orElseThrow());
        book.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow());
        book.setPublisher(publisherRepository.findById(dto.getPublisherId()).orElseThrow());
        return toDto(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public BookDto getBookDtoById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow();
        return toDto(book);
    }

    private BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setPrice(book.getPrice());
        dto.setImageUrl(book.getImageUrl());
        dto.setAuthorId(book.getAuthor() != null ? book.getAuthor().getId() : null);
        dto.setCategoryId(book.getCategory() != null ? book.getCategory().getId() : null);
        dto.setPublisherId(book.getPublisher() != null ? book.getPublisher().getId() : null);
        dto.setAuthorName(book.getAuthor() != null ? book.getAuthor().getName() : null);
        dto.setCategoryName(book.getCategory() != null ? book.getCategory().getCategoryName() : null);
        dto.setPublisherName(book.getPublisher() != null ? book.getPublisher().getPublisherName() : null);
        return dto;
    }
}
