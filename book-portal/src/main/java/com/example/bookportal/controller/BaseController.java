package com.example.bookportal.controller;

import com.example.bookportal.dto.response.ApiResponse;
import com.example.bookportal.dto.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, data, null));
    }

    protected ResponseEntity<ApiResponse<Void>> ok() {
        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }

    protected ResponseEntity<ApiResponse<Void>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResponse<>(false, null, new ApiError(status.value(), message)));
    }

    protected <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(false, data, new ApiError(status.value(), message)));
    }
}
