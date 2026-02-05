package com.example.bookportal.service.impl;

import com.example.bookportal.entity.Publisher;
import com.example.bookportal.exception.ValidationException;
import com.example.bookportal.repository.BookRepository;
import com.example.bookportal.repository.PublisherRepository;
import com.example.bookportal.repository.projection.CategoryBookCountProjection;
import com.example.bookportal.service.PublisherService;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {
    private static final Logger logger = LoggerFactory.getLogger(PublisherServiceImpl.class);

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository,
                                BookRepository bookRepository) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Publisher> getAllPublishers() {
        logger.info("Fetching all publishers");
        return publisherRepository.findAll();
    }

    @Override
    public Publisher getPublisherById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid publisher ID");
        }
        logger.info("Fetching publisher with id: {}", id);
        return publisherRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Publisher not found with id: {}", id);
                    return new RuntimeException("Publisher not found with id: " + id);
                });
    }

    @Override
    public List<CategoryBookCountProjection> getCategoryWiseBooks(Long id) {
        logger.info("Fetching category wise book count for publisher id: {}", id);
        return bookRepository.findCategoryWiseBookCountByPublisher(id);
    }

    @Override
    public long getPublisherBookCount(Long id) {
        logger.info("Fetching book count for publisher id: {}", id);
        return bookRepository.countByPublisherId(id);
    }

    @Override
    public List<com.example.bookportal.entity.Book> getBooksByPublisher(Long publisherId) {
        logger.info("Fetching active books for publisher id: {}", publisherId);
        return bookRepository.findByPublisher_IdAndActiveTrue(publisherId);
    }
}
