package com.example.bookportal.controller;

import com.example.bookportal.service.PublisherService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public String publisherPage(Model model, Authentication auth) {
        model.addAttribute("publishers", publisherService.getAllPublishers());
        model.addAttribute("username", auth.getName());
        return "publisher";
    }

    @GetMapping("/{id}")
    public String publisherSummary(@PathVariable Long id, Model model) {
        model.addAttribute("publisher", publisherService.getPublisherById(id));
        model.addAttribute("categories", publisherService.getCategoryWiseBooks(id));
        return "publisher-summary";
    }
}

