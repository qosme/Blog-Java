package com.proyectoblog.blog.repositories;

import com.proyectoblog.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    //uso JPA para el CRUD
    Optional<User> findByEmail(String email);
}
