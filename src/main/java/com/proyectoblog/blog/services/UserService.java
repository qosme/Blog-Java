package com.proyectoblog.blog.services;

import com.proyectoblog.blog.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserByID(UUID id);

}
