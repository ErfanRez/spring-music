package com.music.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {


    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        log.error("404 Error: {}", ex.getMessage());
        return "404";
    }


    @ExceptionHandler({RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(Exception exception, Model model) {
        String errorMsg = "An unexpected error occurred.";
        if (exception.getMessage() != null) {
            errorMsg = exception.getMessage();
        } else if (exception.getCause() != null) {
            errorMsg = exception.getCause().toString();
        } else {
            errorMsg = exception.toString();
        }
        log.error("500 Error: {}", errorMsg);
        model.addAttribute("errormsg", errorMsg);
        return "error";
    }
}