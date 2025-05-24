package io.framework.oh.controller;

import io.framework.oh.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    
    protected <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    protected <T> ResponseEntity<ApiResponse<T>> error(String message, int status) {
        return ResponseEntity.status(status).body(ApiResponse.error(message, status));
    }
} 