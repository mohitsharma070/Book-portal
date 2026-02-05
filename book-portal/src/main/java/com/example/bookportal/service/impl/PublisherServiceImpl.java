package com.example.bookportal.service.impl;

import com.example.bookportal.entity.Publisher;
import com.example.bookportal.repository.BookRepository;
import com.example.bookportal.repository.PublisherRepository;
import com.example.bookportal.repository.projection.CategoryBookCountProjection;
import com.example.bookportal.service.PublisherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository,
                                BookRepository bookRepository) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @Override
    public Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Publisher not found with id: " + id));
    }

    @Override
    public List<CategoryBookCountProjection> getCategoryWiseBooks(Long id) {
        return bookRepository.findCategoryWiseBookCountByPublisher(id);
    }
}

