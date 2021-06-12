package com.foxminded.university.domain.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
@ControllerAdvice
public class GlobalExceptionController {
    
    private static final String MODEL_MESSAGE = "message";
    private static final String VIEW_ERROR = "error";

    @ExceptionHandler(Exception.class)
    public String handleServiceException(Exception ex, Model model) {
        model.addAttribute(MODEL_MESSAGE, ex.getMessage());
        return VIEW_ERROR;
    }
}
