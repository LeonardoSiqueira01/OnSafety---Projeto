package com.onSafety.Desafio.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleInvalidCpfException(
            InvalidException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("Este Email já foi cadastrado.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
