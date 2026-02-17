package com.proyectoblog.blog.services.implementations;

import com.proyectoblog.blog.domain.entities.Category;
import com.proyectoblog.blog.repositories.CategoryRepository;
import com.proyectoblog.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }
}
