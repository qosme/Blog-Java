package com.proyectoblog.blog.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    //se mapea por author porque en Post esta la variable autor que hace referencia al usuario
    //con cascade all y orphan removal, todos los posts del usuario se eliminan si el usuario es eliminado, para mantener la integridad de la bd
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts =  new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime creationDate;

    //Generar Equals y Hashcode
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(creationDate, user.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, creationDate);
    }

    //Cada vez que una entidad se guarde el valor de la fecha de hoy rellena la variable de fecha de creación, antes de que se guarde en la bd
    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }
}
