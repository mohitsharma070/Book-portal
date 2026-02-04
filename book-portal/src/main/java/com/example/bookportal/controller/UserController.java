package com.example.bookportal.controller;

import com.example.bookportal.dto.ChangePasswordForm;
import com.example.bookportal.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model) {
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(ChangePasswordForm form,
                                 Authentication authentication) {
        userService.changePassword(authentication.getName(), form);
        return "redirect:/dashboard";
    }
}

