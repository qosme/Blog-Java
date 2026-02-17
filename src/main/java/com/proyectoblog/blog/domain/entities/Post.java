package com.proyectoblog.blog.domain.entities;

import com.proyectoblog.blog.domain.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @Column(nullable=false)
    private String title;

    //columnDefinition text para que pueda manejar contenido de dinstinta extensión
    @Column(nullable=false, columnDefinition = "TEXT")
    private String content;

    //Enum en la bd como string para que sea legible por personas
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    //para calcular tiempo de lectura en minutos antes de guardar el post
    @Column(nullable=false)
    private Integer readingTime;

    //fetchtype lazy carga autores solo cuando se acceden explicitamente
    //joincolumn especifica que la columna author_id hace referencia al id del autor/usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id", nullable=false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id", nullable=false)
    private Category category;

    //el set previene duplicados en comparación a una list
    //con jointable hibernate crea la tabla join entre tags y posts, con el nombre y los ids que recibe de c/u
    @ManyToMany
    @JoinTable(
            name="post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @Column(nullable=false)
    private LocalDateTime creationDate;

    @Column(nullable=false)
    private LocalDateTime modificationDate;

    //Generar equals y hashcode

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(title, post.title) && Objects.equals(content, post.content) && status == post.status && Objects.equals(readingTime, post.readingTime) && Objects.equals(creationDate, post.creationDate) && Objects.equals(modificationDate, post.modificationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, status, readingTime, creationDate, modificationDate);
    }

    //para asignar el tiempo de creación que cuenta como ultima modificacion de un post nuevo
    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
    }

    //para actualizar la fecha de modificación
    @PreUpdate
    protected void onUpdate() {
        this.modificationDate = LocalDateTime.now();
    }
}
