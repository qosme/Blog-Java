package com.proyectoblog.blog.controllers;

import com.proyectoblog.blog.domain.dtos.LoginRequest;
import com.proyectoblog.blog.domain.dtos.LoginResponse;
import com.proyectoblog.blog.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    //autenticación de usuario
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String tokenValue = authenticationService.generateToken(userDetails);
        LoginResponse loginResponse = LoginResponse.builder().token(tokenValue).expiresIn(86400).build();
        return ResponseEntity.ok(loginResponse);
    }
}
