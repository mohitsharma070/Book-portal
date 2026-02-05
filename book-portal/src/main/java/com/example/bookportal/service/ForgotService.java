package com.example.bookportal.service;

import com.example.bookportal.dto.ForgotForm;
import com.example.bookportal.entity.User;

public interface ForgotService {
    User findUserByEmail(String email);
    // Add more methods as needed
}
