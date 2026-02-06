package com.example.bookportal.controller;

import com.example.bookportal.dto.SearchRequest;
import com.example.bookportal.dto.SearchResult;
import com.example.bookportal.service.SearchService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SearchController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResult> search(@Valid @RequestBody SearchRequest request) {
        logger.info("Search requested: query={}, type={}, page={}, size={}, sort={}, direction={}",
            request.getQuery(), request.getType(), request.getPage(), request.getSize(), request.getSort(), request.getDirection());
        SearchResult result = searchService.search(request);
        logger.info("Search completed: totalElements={}, totalPages={}, page={}",
                result.getTotalElements(), result.getTotalPages(), result.getPage());
        return ResponseEntity.ok(result);
    }
}
