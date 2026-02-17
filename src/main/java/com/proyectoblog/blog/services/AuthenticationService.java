package com.proyectoblog.blog.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    //para autenticar el usuario
    UserDetails authenticate(String email, String password);

    //para generar token
    String generateToken(UserDetails userDetails);

    //transforma el jwt en detalles de usuario spring security
    UserDetails validateToken(String token);
}
