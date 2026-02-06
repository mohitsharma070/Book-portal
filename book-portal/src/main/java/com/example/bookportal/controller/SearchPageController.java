package com.example.bookportal.controller;

import com.example.bookportal.dto.BookSummaryDTO;
import com.example.bookportal.dto.SearchRequest;
import com.example.bookportal.dto.SearchResult;
import com.example.bookportal.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
public class SearchPageController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SearchPageController.class);

    private final SearchService searchService;

    public SearchPageController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/author")
    public String searchByAuthor(@RequestParam(name = "q", required = false) String query,
                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "size", defaultValue = "20") int size,
                                 Model model) {
        if (isInvalidQuery(query, model)) {
            model.addAttribute("grouped", Collections.emptyMap());
            return "search-author";
        }
        SearchResult result = executeSearch(query, page, size, "author");
        Map<String, List<BookSummaryDTO>> grouped = groupBy(result.getItems(), BookSummaryDTO::getAuthorName, "Unknown Author");
        populateCommon(model, query, result);
        model.addAttribute("grouped", grouped);
        model.addAttribute("heading", "Author");
        logger.info("Author search query='{}' page={} size={}", query, page, size);
        return "search-author";
    }

    @GetMapping("/publisher")
    public String searchByPublisher(@RequestParam(name = "q", required = false) String query,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "20") int size,
                                    Model model) {
        if (isInvalidQuery(query, model)) {
            model.addAttribute("grouped", Collections.emptyMap());
            return "search-publisher";
        }
        SearchResult result = executeSearch(query, page, size, "publisher");
        Map<String, List<BookSummaryDTO>> grouped = groupBy(result.getItems(), BookSummaryDTO::getPublisherName, "Unknown Publisher");
        populateCommon(model, query, result);
        model.addAttribute("grouped", grouped);
        model.addAttribute("heading", "Publisher");
        logger.info("Publisher search query='{}' page={} size={}", query, page, size);
        return "search-publisher";
    }

    @GetMapping("/title")
    public String searchByTitle(@RequestParam(name = "q", required = false) String query,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "20") int size,
                                Model model) {
        if (isInvalidQuery(query, model)) {
            model.addAttribute("items", Collections.emptyList());
            return "search-title";
        }
        SearchResult result = executeSearch(query, page, size, "title");
        populateCommon(model, query, result);
        model.addAttribute("items", result.getItems());
        model.addAttribute("heading", "Title");
        logger.info("Title search query='{}' page={} size={}", query, page, size);
        return "search-title";
    }

    private boolean isInvalidQuery(String query, Model model) {
        if (query == null || query.trim().length() < 2) {
            model.addAttribute("error", "Please enter at least 2 characters to search.");
            model.addAttribute("query", query == null ? "" : query.trim());
            return true;
        }
        model.addAttribute("query", query.trim());
        return false;
    }

    private SearchResult executeSearch(String query, int page, int size, String type) {
        SearchRequest request = new SearchRequest();
        request.setQuery(query.trim());
        request.setPage(Math.max(page, 0));
        request.setSize(Math.min(Math.max(size, 1), 100));
        request.setType(type);
        request.setSort(type);
        request.setDirection("ASC");
        return searchService.search(request);
    }

    private void populateCommon(Model model, String query, SearchResult result) {
        model.addAttribute("query", query.trim());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("pageNumber", result.getPage());
        model.addAttribute("pageSize", result.getSize());
    }

    private <T> Map<String, List<BookSummaryDTO>> groupBy(List<BookSummaryDTO> items,
                                                         Function<BookSummaryDTO, String> keyExtractor,
                                                         String fallback) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyMap();
        }
        return items.stream()
                .collect(Collectors.groupingBy(
                        item -> {
                            String key = keyExtractor.apply(item);
                            return key == null || key.isBlank() ? fallback : key;
                        },
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }
}
