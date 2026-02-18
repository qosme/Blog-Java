package com.proyectoblog.blog.controllers;

import com.proyectoblog.blog.domain.dtos.CreateTagsRequest;
import com.proyectoblog.blog.domain.dtos.TagResponse;
import com.proyectoblog.blog.domain.entities.Tag;
import com.proyectoblog.blog.mappers.TagMapper;
import com.proyectoblog.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<Tag> tags = tagService.getTags();

        List<TagResponse> tagResponses = tags.stream().map(tag -> tagMapper.toTagResponse(tag)).toList();

        return ResponseEntity.ok(tagResponses);
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@RequestBody CreateTagsRequest createTagsRequest) {
        List<Tag> savedTags = tagService.createTags(createTagsRequest.getNames());

        //convertir tags guardadas en tagResponse
        List<TagResponse> createdTagResponse = savedTags.stream().map(tag -> tagMapper.toTagResponse(tag)).toList();

        return new ResponseEntity<>(createdTagResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
