package com.proyectoblog.blog.mappers;

import com.proyectoblog.blog.domain.CreatePostRequest;
import com.proyectoblog.blog.domain.UpdatePostRequest;
import com.proyectoblog.blog.domain.dtos.CreatePostRequestDTO;
import com.proyectoblog.blog.domain.dtos.PostDTO;
import com.proyectoblog.blog.domain.dtos.UpdatePostRequestDTO;
import com.proyectoblog.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDTO toDto(Post post);
    CreatePostRequest toCreatePostRequest(CreatePostRequestDTO createPostRequestDTO);
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDTO updatePostRequestDTO);
}
