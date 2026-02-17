package com.proyectoblog.blog.controllers;

import com.proyectoblog.blog.domain.dtos.CategoryDTO;
import com.proyectoblog.blog.domain.entities.Category;
import com.proyectoblog.blog.mappers.CategoryMapper;
import com.proyectoblog.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> listCategories() {
        List<CategoryDTO> categories = categoryService.listCategories().stream().
                map(category -> categoryMapper.toDTO(category)).toList();

        return ResponseEntity.ok(categories);
    }
}
