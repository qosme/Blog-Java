package com.proyectoblog.blog.controllers;

import com.proyectoblog.blog.domain.dtos.ApiErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {

    //Manejo de excepciones
    @ExceptionHandler(Exception.class)
    public ResponseEntity <ApiErrorResponse> handleException(Exception e) {
        log.error("Excepción encontrada", e);
        ApiErrorResponse errorResponse = ApiErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value()).
                message("Ha ocurrido un error inesperado").build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Excepción de illegal argument
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value()).
                message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //Excepción de illegal state
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(IllegalStateException e) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder().status(HttpStatus.CONFLICT.value()).
                message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    //Excepción de bad credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder().status(HttpStatus.UNAUTHORIZED.value()).
                message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    //Excepción de entidad no encontrada
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder().status(HttpStatus.NOT_FOUND.value()).
                message(e.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
