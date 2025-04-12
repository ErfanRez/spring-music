package com.music.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

//@ControllerAdvice
public class RestExceptionHandler {



//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        BindingResult bindingResult = ex.getBindingResult();
//
//        Map<String, String> errorDetails = new HashMap<>();
//        bindingResult.getFieldErrors().forEach(error ->
//                errorDetails.put(error.getField(), error.getDefaultMessage())
//        );
//
//        ErrorResponse errorResponse = new ErrorResponse(
//                "error",
//                "input validation failed",
//                errorDetails
//        );
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuleExceptions(Exception ex) {
        String errorMessage = ex.getLocalizedMessage();


        ErrorResponse errorResponse = new ErrorResponse(
                "error",
                errorMessage,
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex) {
        String errorMessage = ex.getMessage();


        ErrorResponse errorResponse = new ErrorResponse(
                "error",
                "An unexpected error occurred: " + errorMessage,
                null
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
