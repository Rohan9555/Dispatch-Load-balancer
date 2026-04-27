package com.dispatch.dispatch_load_balancer.Controllers;

import com.dispatch.dispatch_load_balancer.dto.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse handleIllegalArgument(IllegalArgumentException ex) {
        return new ApiResponse(ex.getMessage(), "error");
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleGeneral(Exception ex) {
        return new ApiResponse("Something went wrong: " + ex.getMessage(), "error");
    }
}