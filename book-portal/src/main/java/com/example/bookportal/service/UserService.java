package com.example.bookportal.service;

import com.example.bookportal.dto.ChangePasswordForm;
import com.example.bookportal.dto.EditProfileForm;
import com.example.bookportal.dto.RegisterForm;
import com.example.bookportal.entity.User;
import com.example.bookportal.exception.ResourceNotFoundException;
import com.example.bookportal.exception.ValidationException;
import com.example.bookportal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= EXISTING LOGIC (UNCHANGED) =================

    public void register(RegisterForm form) {

        logger.info("Registering user: {}", form.getUsername());

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            logger.warn("Password mismatch for user: {}", form.getUsername());
            throw new ValidationException("Passwords do not match");
        }

        if (userRepository.findByUsername(form.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setFullName(form.getFirstName() + " " + form.getLastName());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setSecretAnswer(form.getSecretAnswer());
        user.setSecretQuestion(form.getSecretQuestion());

        userRepository.save(user);
        logger.info("User registered: {}", form.getUsername());
    }

    public void changePassword(String username, ChangePasswordForm form) {
        logger.info("Password change attempt for user: {}", username);

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.warn("User not found for password change: {}", username);
            return new ResourceNotFoundException("User not found: " + username);
        });

        if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
            logger.warn("Invalid old password for user: {}", username);
            throw new ValidationException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
        logger.info("Password changed for user: {}", username);
    }

    // ================= NEW METHODS (ADDED ONLY) =================

    public User findByUsername(String username) {
        logger.info("Finding user by username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", username);
                    return new ResourceNotFoundException("User not found: " + username);
                });
    }

    public void updateProfileFromForm(EditProfileForm form) {
        logger.info("Updating profile for user: {}", form.getUsername());

        User user = userRepository.findByUsername(form.getUsername())
                .orElseThrow(() -> {
                    logger.warn("User not found for profile update: {}", form.getUsername());
                    return new ResourceNotFoundException("User not found");
                });

        user.setEmail(form.getEmail());
        user.setFullName(form.getFirstName() + " " + form.getLastName());
        user.setSecretQuestion(form.getSecretQuestion());
        user.setSecretAnswer(form.getSecretAnswer());

        userRepository.save(user);
        logger.info("Profile updated for user: {}", form.getUsername());
    }
}
