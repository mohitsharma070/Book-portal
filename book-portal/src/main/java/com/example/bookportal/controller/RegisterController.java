package com.example.bookportal.controller;

import com.example.bookportal.dto.RegisterForm;
import com.example.bookportal.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/register")
@Validated
public class RegisterController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        logger.info("Navigated to register page");
        return "register";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute RegisterForm form,
                           BindingResult bindingResult,
                           Model model,
                           org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        logger.info("Register attempt: username={}, email={}", form.getUsername(), form.getEmail());
        if (bindingResult.hasErrors()) {
            logger.warn("Validation failed: {}", bindingResult.getAllErrors());
            model.addAttribute("registerForm", form);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "register";
        }
        try {
            registerService.register(form);
            logger.info("Registration successful for username={}", form.getUsername());
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login-info";
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            logger.warn("Duplicate registration attempt: {}", ex.getMessage());
            model.addAttribute("registerForm", form);
            model.addAttribute("error", "Username or email already exists.");
            return "register";
        } catch (Exception ex) {
            logger.error("Registration failed: {}", ex.getMessage(), ex);
            model.addAttribute("registerForm", form);
            model.addAttribute("error", "Registration failed: " + ex.getMessage());
            return "register";
        }
    }
}
