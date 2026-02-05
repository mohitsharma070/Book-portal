package com.example.bookportal.service.impl;

import com.example.bookportal.dto.ForgotForm;
import com.example.bookportal.entity.User;
import com.example.bookportal.repository.UserRepository;
import com.example.bookportal.service.ForgotService;
import org.springframework.stereotype.Service;

@Service
public class ForgotServiceImpl implements ForgotService {
    private final UserRepository userRepository;

    public ForgotServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
