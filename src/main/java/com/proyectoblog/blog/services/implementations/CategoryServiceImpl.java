package com.proyectoblog.blog.services.implementations;

import com.proyectoblog.blog.domain.entities.Category;
import com.proyectoblog.blog.repositories.CategoryRepository;
import com.proyectoblog.blog.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    //transactional para que las llamadas multiples a la bd se realicen en una transacción
    @Override
    @Transactional
    public Category createCategory(Category category) {
        //si la categoria existe
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("La categoría " + category.getName() + " ya existe");
        }
        //si la categoria no existe, se guarda
        return categoryRepository.save(category);
    }

    //no se pueden eliminar categorías con posts asociados
    @Override
    public void deleteCategory(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            if(category.get().getPosts().size() > 0) {
                throw new IllegalStateException("La categoría tiene posts asociados");
            }
            categoryRepository.deleteById(id);
        }
    }

    @Override
    public Category getCategoryByID(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("La categoría con el id "+id+" no existe"));
    }
}
