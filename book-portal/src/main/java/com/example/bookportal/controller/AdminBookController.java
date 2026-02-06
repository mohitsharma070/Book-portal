package com.example.bookportal.controller;

import com.example.bookportal.dto.BookDto;
import com.example.bookportal.service.AdminBookService;
import com.example.bookportal.repository.AuthorRepository;
import com.example.bookportal.repository.CategoryRepository;
import com.example.bookportal.repository.PublisherRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminBookController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AdminBookController.class);
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AdminBookService adminBookService;

    public AdminBookController(AdminBookService adminBookService,
                               AuthorRepository authorRepository,
                               CategoryRepository categoryRepository,
                               PublisherRepository publisherRepository) {
        this.adminBookService = adminBookService;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
    }

    @GetMapping("/admin-pannel-add")
    public String addBookPage(Model model) {
        logger.info("Accessed add book page");
        model.addAttribute("bookDto", new BookDto());
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
        return "admin-pannel-add";
    }

    @PostMapping("/admin-pannel-add")
    public String addBook(@Valid @ModelAttribute("bookDto") BookDto bookDto,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        logger.info("Add book attempt: title={}, authorId={}, categoryId={}, publisherId={}", bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getCategoryId(), bookDto.getPublisherId());
        if (bindingResult.hasErrors()) {
            logger.warn("Add book validation failed: {}", bindingResult.getAllErrors());
            model.addAttribute("bookDto", bookDto);
            model.addAttribute("authors", authorRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("publishers", publisherRepository.findAll());
            model.addAttribute("error", "Validation failed. Please check your input.");
            return "admin-pannel-add";
        }
        try {
            adminBookService.createBook(bookDto);
            logger.info("Book added successfully: title={}", bookDto.getTitle());
            redirectAttributes.addFlashAttribute("success", "Book added successfully.");
            return "redirect:/admin-pannel";
        } catch (Exception ex) {
            logger.error("Add book failed: {}", ex.getMessage(), ex);
            model.addAttribute("bookDto", bookDto);
            model.addAttribute("authors", authorRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("publishers", publisherRepository.findAll());
            model.addAttribute("error", ex.getMessage());
            return "admin-pannel-add";
        }
    }

    @GetMapping("/admin-pannel-update")
    public String updateBookPage(@RequestParam("id") Long id, Model model) {
        logger.info("Accessed update book page for id={}", id);
        BookDto bookDto = adminBookService.getBookDtoById(id);
        model.addAttribute("bookDto", bookDto);
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
        return "admin-pannel-update";
    }

    @PostMapping("/admin-pannel-update")
    public String updateBook(@Valid @ModelAttribute("bookDto") BookDto bookDto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        logger.info("Update book attempt: id={}, title={}", bookDto.getId(), bookDto.getTitle());
        if (bindingResult.hasErrors()) {
            logger.warn("Update book validation failed: {}", bindingResult.getAllErrors());
            model.addAttribute("bookDto", bookDto);
            model.addAttribute("authors", authorRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("publishers", publisherRepository.findAll());
            model.addAttribute("error", "Validation failed. Please check your input.");
            return "admin-pannel-update";
        }
        try {
            adminBookService.updateBook(bookDto.getId(), bookDto);
            logger.info("Book updated successfully: id={}, title={}", bookDto.getId(), bookDto.getTitle());
            redirectAttributes.addFlashAttribute("success", "Book updated successfully.");
            return "redirect:/admin-pannel";
        } catch (Exception ex) {
            logger.error("Update book failed: {}", ex.getMessage(), ex);
            model.addAttribute("bookDto", bookDto);
            model.addAttribute("authors", authorRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("publishers", publisherRepository.findAll());
            model.addAttribute("error", ex.getMessage());
            return "admin-pannel-update";
        }
    }
}
