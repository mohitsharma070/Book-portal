package com.example.bookportal.controller;

import com.example.bookportal.dto.ForgotForm;
import com.example.bookportal.entity.User;
import com.example.bookportal.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ForgotController {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public ForgotController(UserRepository userRepository,
                           org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // STEP 1: Show Forgot Page
    @GetMapping("/forgot")
    public String forgotPage(Model model) {
        model.addAttribute("forgotForm", new ForgotForm());
        return "forgot";   // old forgot.html
    }

    @PostMapping("/forgot")
    public String processForgot(ForgotForm form, Model model) {
        User user = userRepository.findByEmail(form.getEmail()).orElse(null);
        if (user == null) {
            model.addAttribute("error", "No account found with this email");
            return "forgot";
        }
        // Only show secret question, not answer or other info
        model.addAttribute("secretQuestion", user.getSecretQuestion());
        com.example.bookportal.dto.ForgotAnswerForm answerForm = new com.example.bookportal.dto.ForgotAnswerForm();
        answerForm.setEmail(user.getEmail());
        model.addAttribute("forgotAnswerForm", answerForm);
        return "forgot-answer";
    }

    @PostMapping("/forgot/verify")
    public String verifySecretAnswer(com.example.bookportal.dto.ForgotAnswerForm form, Model model) {
        User user = userRepository.findByEmail(form.getEmail()).orElse(null);
        if (user == null) {
            model.addAttribute("error", "Session expired. Please start again.");
            return "forgot";
        }
        if (!user.getSecretAnswer().equalsIgnoreCase(form.getSecretAnswer())) {
            model.addAttribute("secretQuestion", user.getSecretQuestion());
            model.addAttribute("forgotAnswerForm", form);
            model.addAttribute("error", "Incorrect answer. Try again.");
            return "forgot-answer";
        }
        // Answer correct, show reset form
        form.setEmail(user.getEmail());
        model.addAttribute("forgotAnswerForm", form);
        return "forgot-reset";
    }

    @PostMapping("/forgot/reset")
    public String resetPassword(com.example.bookportal.dto.ForgotAnswerForm form, Model model) {
        User user = userRepository.findByEmail(form.getEmail()).orElse(null);
        if (user == null) {
            model.addAttribute("error", "Session expired. Please start again.");
            return "forgot";
        }
        if (form.getNewPassword() == null || form.getNewPassword().isEmpty() ||
            !form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("forgotAnswerForm", form);
            model.addAttribute("error", "Passwords do not match or are empty.");
            return "forgot-reset";
        }
        // Use PasswordEncoder bean for hashing
        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
        model.addAttribute("success", "Password reset successful. Please login.");
        return "forgot-reset";
    }
}
