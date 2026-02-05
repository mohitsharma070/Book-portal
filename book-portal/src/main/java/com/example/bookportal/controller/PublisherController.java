package com.example.bookportal.controller;

import com.example.bookportal.service.PublisherService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String publisherSummary(@PathVariable Long id, Model model) {
        model.addAttribute("publisher", publisherService.getPublisherById(id));
        model.addAttribute("bookCount", publisherService.getPublisherBookCount(id));
        model.addAttribute("books", publisherService.getBooksByPublisher(id));
        model.addAttribute("categories", publisherService.getCategoryWiseBooks(id));
        logger.info("Fetched details for publisher ID: {}", id);
        return "publisher-summary";
    }
}
