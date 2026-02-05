package com.example.bookportal.service;

import com.example.bookportal.entity.Publisher;
import com.example.bookportal.repository.projection.CategoryBookCountProjection;

import java.util.List;

public interface PublisherService {

    List<Publisher> getAllPublishers();

    Publisher getPublisherById(Long id);

    List<CategoryBookCountProjection> getCategoryWiseBooks(Long id);
}

