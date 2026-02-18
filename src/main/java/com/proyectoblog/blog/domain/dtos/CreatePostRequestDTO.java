package com.proyectoblog.blog.domain.dtos;

import com.proyectoblog.blog.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostRequestDTO {
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 100, message = "El título debe tener entre {min} y {max} caracteres")
    private String title;

    @NotBlank(message = "El contenido es obligatorio")
    @Size(min = 5, max = 50000, message = "El contenido debe tener entre {min} y {max} caracteres")
    private String content;

    @NotNull(message = "El ID de la categoría es obligatorio")
    private UUID categoryID;

    @Builder.Default
    @Size(max = 20, message = "Los posts pueden tener hasta {max} etiquetas")
    private Set<UUID> tagIDs = new HashSet<>();

    @NotNull(message = "El estado del post es obligatorio")
    private PostStatus status;
}
