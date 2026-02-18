package com.proyectoblog.blog.services.implementations;

import com.proyectoblog.blog.domain.entities.Tag;
import com.proyectoblog.blog.repositories.TagRepository;
import com.proyectoblog.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
        //ver si hay tags existentes antes de crearla
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);

        //extraer los nombres de los tags existentes
        Set<String> existingTagNames = existingTags.stream().map(tag -> tag.getName()).collect(Collectors.toSet());

        //filtrar los tags para que solo se puedan crear nombres nuevos
        //y generar lista vacia para crear tags nuevos
        List<Tag> newTags = tagNames.stream().filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder().name(name).posts(new HashSet<>()).build()).collect(Collectors.toList());

        //guardar tags
        List<Tag> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()) {
            savedTags = tagRepository.saveAll(newTags);
        }

        //combinar tags existentes y guardadas
        savedTags.addAll(existingTags);

        return savedTags;

    }

    @Transactional
    @Override
    public void deleteTag(UUID id) {
        //el tag no debe estar relacionado con posts
        tagRepository.findById(id).ifPresent(tag -> {
            //si existen posts asociados
            if(!tag.getPosts().isEmpty()) {
                throw new IllegalArgumentException("No se puede borrar la etiqueta porque tiene posts relacionados");
            }
            //si no hay posts asociados
            tagRepository.deleteById(id);
        });
    }

    @Override
    public Tag getTagByID(UUID id) {
        return tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("El tag con el id "+id+" no existe"));
    }

    @Override
    public List<Tag> getTagByIDs(Set<UUID> tagIDs) {
        List<Tag> foundTags = tagRepository.findAllById(tagIDs);
        if(foundTags.size() != tagIDs.size()) {
            throw new EntityNotFoundException("No todas las etiquetas especificadas existen");
        }
        return foundTags;
    }
}
