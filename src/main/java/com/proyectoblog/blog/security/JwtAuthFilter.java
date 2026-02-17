package com.proyectoblog.blog.security;

import com.proyectoblog.blog.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = extractToken(request);
            if (token != null) {
                UserDetails userDetails = authenticationService.validateToken(token);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                if(userDetails instanceof BlogUserDetails) {
                    request.setAttribute("user_id", ((BlogUserDetails) userDetails).getId());
                }
            }
        }
        catch (Exception e) {
            //sin excepciones, no autenticar el usuario si el token es invalido
            log.warn("Se ha recibido un token inválido"); //log con slf4j
        }

        filterChain.doFilter(request, response);
    }

    //implementación para extraer token
    private String extractToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Autorización");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        //si el token es nulo
        return null;
    }
}
