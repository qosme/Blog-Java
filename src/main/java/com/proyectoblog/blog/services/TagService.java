package com.proyectoblog.blog.services;

import com.proyectoblog.blog.domain.entities.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {
    List<Tag> getTags();
    List<Tag> createTags(Set<String> tagNames);
    void deleteTag(UUID id);
    Tag getTagByID(UUID id);
    List<Tag> getTagByIDs(Set<UUID> tagIDs);
}
