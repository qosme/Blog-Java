package com.proyectoblog.blog.services.implementations;

import com.proyectoblog.blog.domain.CreatePostRequest;
import com.proyectoblog.blog.domain.PostStatus;
import com.proyectoblog.blog.domain.UpdatePostRequest;
import com.proyectoblog.blog.domain.entities.Category;
import com.proyectoblog.blog.domain.entities.Post;
import com.proyectoblog.blog.domain.entities.Tag;
import com.proyectoblog.blog.domain.entities.User;
import com.proyectoblog.blog.repositories.PostRepository;
import com.proyectoblog.blog.services.CategoryService;
import com.proyectoblog.blog.services.PostService;
import com.proyectoblog.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;
    private static final int WORDS_PER_MINUTE = 200;

    @Override
    public Post getPost(UUID id) {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("El post con el id: "+id+" no existe"));
    }

    @Override
    @Transactional(readOnly = true) //transactional de springframework
    public List<Post> getAllPosts(UUID categoryID, UUID tagID) {
        //si existen tagid y categoryid
        //chequear valores de categoryID y tagID
        if(categoryID != null && tagID != null){
            //devolver todos los posts publicados que comparten catID y tagID
            Category category = categoryService.getCategoryByID(categoryID);
            Tag tag = tagService.getTagByID(tagID);

            //filtrar posts por cat y tag
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        }
        //si solo el id de categoria existe
        if(categoryID != null) {
            Category category = categoryService.getCategoryByID(categoryID);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }

        //si solo el id de tag existe
        if(tagID != null) {
            Tag tag = tagService.getTagByID(tagID);
            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
        }

        //si ambos son nulos
        //devolver todos los posts publicados
        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Transactional
    @Override
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));
        //especificar categoria para el post
        Category category = categoryService.getCategoryByID(createPostRequest.getCategoryID());
        newPost.setCategory(category);
        //especificar tags
        Set<UUID> tagIDs = createPostRequest.getTagIDs();
        List<Tag> tags = tagService.getTagByIDs(tagIDs);
        newPost.setTags(new HashSet<>(tags));

        return  postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("El post con id: "+id+" no existe"));
        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));
        //actualizar categoria si cambió
        UUID updatePostRequestCategoryID = updatePostRequest.getCategoryID();
        //si el request de actualizacion da un category id diferente al que tiene
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryID)) {
            Category newCategory = categoryService.getCategoryByID(updatePostRequestCategoryID);
            existingPost.setCategory(newCategory);
        }
        //actualizar tags
        Set<UUID> existingTagIDs = existingPost.getTags().stream().map(tag -> tag.getId()).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIDs = updatePostRequest.getTagIDs();
        //si los tags ids de la actualizacion son diferentes, se actualizan
        if(!existingTagIDs.equals(updatePostRequestTagIDs)) {
            List<Tag> newTags = tagService.getTagByIDs(updatePostRequestTagIDs);
            existingPost.setTags(new HashSet<>(newTags));
        }
        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(UUID id) {
        //ver si el post existe
        Post post = getPost(id);
        //eliminar
        postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content) {
        if(content == null || content.isEmpty()){
            return 0;
        }
        //cortar por espacios
        int wordCount =content.trim().split("\\s+").length;

        //palabras por minuto
        return (int) Math.ceil((double)wordCount/WORDS_PER_MINUTE);
    }
}
