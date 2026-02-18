package com.proyectoblog.blog.services;

import com.proyectoblog.blog.domain.CreatePostRequest;
import com.proyectoblog.blog.domain.UpdatePostRequest;
import com.proyectoblog.blog.domain.entities.Post;
import com.proyectoblog.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post getPost(UUID id);
    List<Post> getAllPosts(UUID categoryID, UUID tagID);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    void deletePost(UUID id);
    //no va el usuario, porque no se actualiza el usuario
}
