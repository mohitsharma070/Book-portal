package com.example.bookportal.controller;

import com.example.bookportal.dto.ChangePasswordForm;
import com.example.bookportal.dto.EditProfileForm;
import com.example.bookportal.entity.User;
import com.example.bookportal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        logger.info("Dashboard accessed by user: {}", auth.getName());
        model.addAttribute("username", auth.getName());
        model.addAttribute(
                "today",
                LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
        );
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("role", "ADMIN");
        } else {
            model.addAttribute("role", "USER");
        }
        return "dashboard";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model) {
        logger.info("Change password page accessed");
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            Authentication auth,
            @ModelAttribute ChangePasswordForm form,
            Model model,
            HttpServletRequest request) {
        logger.info("Password change attempt for user: {}", auth.getName());
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            logger.warn("Password mismatch for user: {}", auth.getName());
            model.addAttribute("changePasswordForm", form);
            model.addAttribute("error", "New password and confirm password do not match.");
            return "change-password";
        }
        try {
            userService.changePassword(auth.getName(), form);
            logger.info("Password changed for user: {}", auth.getName());
            request.getSession().invalidate();
            model.addAttribute("success", "Password changed successfully. Please log in again.");
            return "change-password";
        } catch (Exception ex) {
            logger.error("Password change failed: {}", ex.getMessage(), ex);
            model.addAttribute("changePasswordForm", form);
            model.addAttribute("error", ex.getMessage());
            return "change-password";
        }
    }

    @GetMapping("/user-options")
    public String userOptions(Authentication auth, Model model) {
        logger.info("User options accessed by user: {}", auth.getName());
        model.addAttribute("username", auth.getName());
        model.addAttribute("today",
                LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        return "user-options";
    }

    @GetMapping("/edit-profile")
    public String editProfile(Model model, Principal principal) {
        logger.info("Edit profile page accessed by user: {}", principal.getName());
        User user = userService.findByUsername(principal.getName());
        EditProfileForm form = new EditProfileForm();
        form.setUsername(user.getUsername());
        form.setEmail(user.getEmail());
        form.setSecretQuestion(user.getSecretQuestion());
        form.setSecretAnswer(user.getSecretAnswer());
        if (user.getFullName() != null) {
            String[] parts = user.getFullName().split(" ", 2);
            form.setFirstName(parts[0]);
            if (parts.length > 1) {
                form.setLastName(parts[1]);
            }
        }
        model.addAttribute("editProfileForm", form);
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateProfile(
            @ModelAttribute EditProfileForm form,
            RedirectAttributes redirectAttributes) {
        logger.info("Profile update attempt for user: {}", form.getUsername());
        userService.updateProfileFromForm(form);
        logger.info("Profile updated for user: {}", form.getUsername());
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
        return "redirect:/edit-profile";
    }

    @GetMapping("/admin-pannel")
    public String adminPanel(Authentication auth, Model model) {
        if (auth == null || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/?error=unauthorized";
        }
        model.addAttribute("username", auth.getName());
        return "admin-pannel";
    }

}