package com.proyectoblog.blog.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryRequest {

    //para hacer validaciones

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre de la categoría debe tener entre {min} y {max} caracteres")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "El nombre de la categoría solo puede contener letras, números, espacios y guiones")
    private String name;

}
