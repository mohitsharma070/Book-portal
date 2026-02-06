package com.example.bookportal.service;

import com.example.bookportal.entity.Publisher;
import com.example.bookportal.repository.projection.CategoryBookCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PublisherService {

    List<Publisher> getAllPublishers();

    Publisher getPublisherById(Long id);

    List<CategoryBookCountProjection> getCategoryWiseBooks(Long id);

    long getPublisherBookCount(Long id);


    Page<com.example.bookportal.entity.Book> getBooksByPublisher(Long publisherId, Pageable pageable);
}
