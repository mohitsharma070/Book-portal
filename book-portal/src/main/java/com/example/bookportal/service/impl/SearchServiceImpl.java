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
        logger.info("SearchServiceImpl.search called: query={}, page={}, size={}, sort={}, direction={}",
                request.getQuery(), request.getPage(), request.getSize(), request.getSort(), request.getDirection());
        String q = request.getQuery();

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()),
                request.getSort() == null ? "id" : request.getSort());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<Book> page = bookRepository.findAll(BookSpecification.containsTextInAllFields(q), pageable);
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
}
