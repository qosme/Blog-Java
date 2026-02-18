package com.proyectoblog.blog.controllers;

import com.proyectoblog.blog.domain.CreatePostRequest;
import com.proyectoblog.blog.domain.UpdatePostRequest;
import com.proyectoblog.blog.domain.dtos.CreatePostRequestDTO;
import com.proyectoblog.blog.domain.dtos.PostDTO;
import com.proyectoblog.blog.domain.dtos.UpdatePostRequestDTO;
import com.proyectoblog.blog.domain.entities.Post;
import com.proyectoblog.blog.domain.entities.User;
import com.proyectoblog.blog.mappers.PostMapper;
import com.proyectoblog.blog.services.PostService;
import com.proyectoblog.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(
            //filtrar posts por categoria y/o tags
            @RequestParam(required = false) UUID categoryID,
            @RequestParam(required = false) UUID tagID) {

        List<Post> posts = postService.getAllPosts(categoryID, tagID);
        List<PostDTO> postDTOs = posts.stream().map(post -> postMapper.toDto(post)).toList();

        //para obtener todos los posts
        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDTO>> getDraftPosts(@RequestAttribute UUID userID) {
        //para acceder a los drafts/borradores el usuario necesita estar logueado
        User loggedUser = userService.getUserByID(userID);

        //una vez logueado, se muestran los borradores
        List<Post> draftPosts = postService.getDraftPosts(loggedUser);

        List<PostDTO> postDTOs = draftPosts.stream().map(post -> postMapper.toDto(post)).toList();

        return ResponseEntity.ok(postDTOs);
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(
            //para json
            @Valid @RequestBody CreatePostRequestDTO createPostRequestDTO,
            @RequestAttribute UUID userID) {
        //usuario debe estar logueado
        User loggedUser = userService.getUserByID(userID);

        //representación de la info de creación de post para la capa de servicio
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDTO);

        Post createdPost = postService.createPost(loggedUser, createPostRequest);

        PostDTO createdPostDTO = postMapper.toDto(createdPost);

        return new ResponseEntity<>(createdPostDTO, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDTO> updatePost(
                    @PathVariable UUID id,
                    //representar el request de actualizacion
                    @Valid @RequestBody UpdatePostRequestDTO updatePostRequestDTO
            ) {
        //convertir updatepostequestdto a updatepostrequest para pasarlo a la capa de service
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDTO);
        //metodo de actualizacion
        Post updatedPost = postService.updatePost(id, updatePostRequest);
        //convertir la actualización en dto
        PostDTO updatedPostDTO = postMapper.toDto(updatedPost);

        return ResponseEntity.ok(updatedPostDTO);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable UUID id) {
        Post post = postService.getPost(id);
        PostDTO postDTO= postMapper.toDto(post);
        return ResponseEntity.ok(postDTO);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
