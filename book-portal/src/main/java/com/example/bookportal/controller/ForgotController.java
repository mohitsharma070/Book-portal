package com.example.bookportal.controller;

import com.example.bookportal.dto.ForgotForm;
import com.example.bookportal.entity.User;
import com.example.bookportal.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ForgotController {

    private final UserRepository userRepository;

    public ForgotController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Step 1: Show forgot page
    @GetMapping("/forgot")
    public String forgotPage(Model model) {
        model.addAttribute("forgotForm", new ForgotForm());
        return "forgot";
    }

    // Step 2: Process forgot logic
    @PostMapping("/forgot")
    public String processForgot(ForgotForm form, Model model) {

        User user = userRepository.findByEmail(form.getEmail()).orElse(null);

        if (user == null) {
            model.addAttribute("error", "No account found with this email");
            return "forgot";
        }

        model.addAttribute("username", user.getUsername());
        return "forgot-result";
    }
}
