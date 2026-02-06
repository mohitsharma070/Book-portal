package com.example.bookportal.controller;

import com.example.bookportal.service.PublisherService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/publishers")
@Validated
public class PublisherController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PublisherController.class);

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public String publisherPage(Model model, Authentication auth) {
        model.addAttribute("publishers", publisherService.getAllPublishers());
        model.addAttribute("username", auth.getName());
        logger.info("Fetched all publishers for user: {}", auth.getName());
        return "publisher";
    }

    @GetMapping("/{id}")
    public String publisherSummary(@PathVariable Long id, Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sort,
                                  @RequestParam(defaultValue = "ASC") String direction) {
        model.addAttribute("publisher", publisherService.getPublisherById(id));
        model.addAttribute("bookCount", publisherService.getPublisherBookCount(id));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        Page<com.example.bookportal.entity.Book> booksPage = publisherService.getBooksByPublisher(id, pageable);
        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("categories", publisherService.getCategoryWiseBooks(id));
        model.addAttribute("page", booksPage);
        logger.info("Fetched details for publisher ID: {}", id);
        return "publisher-summary";
    }
}
