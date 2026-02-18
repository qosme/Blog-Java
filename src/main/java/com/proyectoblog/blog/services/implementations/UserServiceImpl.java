package com.proyectoblog.blog.services.implementations;

import com.proyectoblog.blog.domain.entities.User;
import com.proyectoblog.blog.repositories.UserRepository;
import com.proyectoblog.blog.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    //por interactuar con la bd
    private final UserRepository userRepository;

    @Override
    public User getUserByID(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("El usuario con id: "+id+" no existe"));
    }
}
