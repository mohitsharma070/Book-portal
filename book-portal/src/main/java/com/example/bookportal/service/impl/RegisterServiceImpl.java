package com.example.bookportal.service.impl;

import com.example.bookportal.dto.RegisterForm;
import com.example.bookportal.entity.User;
import com.example.bookportal.repository.UserRepository;
import com.example.bookportal.service.RegisterService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RegisterServiceImpl implements RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void register(RegisterForm form) {
        logger.info("Service: Registering user: username={}, email={}", form.getUsername(), form.getEmail());
        if (userRepository.findByUsername(form.getUsername()).isPresent()) {
            logger.warn("Service: Username already exists: {}", form.getUsername());
            throw new org.springframework.dao.DataIntegrityViolationException("Username already exists");
        }
        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            logger.warn("Service: Email already exists: {}", form.getEmail());
            throw new org.springframework.dao.DataIntegrityViolationException("Email already exists");
        }
        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setFullName(form.getFirstName() + " " + form.getLastName());
        user.setSecretQuestion(form.getSecretQuestion());
        user.setSecretAnswer(form.getSecretAnswer());
        user.setActive(true);
        user.setEnabled(true);
        userRepository.save(user);
        logger.info("Service: User registered successfully: username={}", form.getUsername());
    }
}
