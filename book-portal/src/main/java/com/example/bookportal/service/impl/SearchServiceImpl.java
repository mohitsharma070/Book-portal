package com.example.bookportal.service.impl;

import com.example.bookportal.dto.BookSummaryDTO;
import com.example.bookportal.dto.SearchRequest;
import com.example.bookportal.dto.SearchResult;
import com.example.bookportal.entity.Book;
import com.example.bookportal.repository.BookRepository;
import com.example.bookportal.service.SearchService;
import com.example.bookportal.specification.BookSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final BookRepository bookRepository;

    public SearchServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public SearchResult search(SearchRequest request) {
        logger.info("SearchServiceImpl.search called: query={}, type={}, page={}, size={}, sort={}, direction={}",
                request.getQuery(), request.getType(), request.getPage(), request.getSize(), request.getSort(), request.getDirection());
        String q = request.getQuery();
        String type = normalizeType(request.getType());

        Pageable pageable = PageRequest.of(
                Math.max(request.getPage(), 0),
                request.getSize(),
                resolveSort(request.getSort(), type, request.getDirection())
        );

        Specification<Book> specification = BookSpecification.active()
                .and(BookSpecification.containsText(q, type));

        Page<Book> page = bookRepository.findAll(specification, pageable);
        logger.info("SearchServiceImpl.search result: totalElements={}, totalPages={}, page={}",
                page.getTotalElements(), page.getTotalPages(), page.getNumber());

        List<BookSummaryDTO> items = page.stream().map(b -> new BookSummaryDTO(
                b.getId(),
                b.getTitle(),
                b.getAuthor() != null ? b.getAuthor().getName() : null,
                b.getCategory() != null ? b.getCategory().getCategoryName() : null,
                b.getPublisher() != null ? b.getPublisher().getPublisherName() : null,
                b.getPrice(),
                b.getImageUrl()
        )).collect(Collectors.toList());

        return new SearchResult(items, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
    }

        private Sort resolveSort(String sortField, String type, String direction) {
                Sort.Direction dir = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.ASC);
                String requested = (sortField != null && !sortField.isBlank()) ? sortField : type;
                String property;
                switch (requested.toUpperCase()) {
                        case "AUTHOR" -> property = "author.name";
                        case "PUBLISHER" -> property = "publisher.publisherName";
                        case "CATEGORY" -> property = "category.categoryName";
                        default -> property = "title";
                }
                return Sort.by(dir, property);
        }

        private String normalizeType(String type) {
                return type == null ? "ALL" : type.trim().toUpperCase();
        }
}
