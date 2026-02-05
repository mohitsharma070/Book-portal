package com.example.bookportal.service;

import com.example.bookportal.dto.SearchRequest;
import com.example.bookportal.dto.SearchResult;

public interface SearchService {
    SearchResult search(SearchRequest request);
}
