package com.proyectoblog.blog.mappers;

import com.proyectoblog.blog.domain.PostStatus;
import com.proyectoblog.blog.domain.dtos.CategoryDTO;
import com.proyectoblog.blog.domain.entities.Category;
import com.proyectoblog.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

//si no puede mapear las categorias va a ignorarlas, y no mostrar error
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    //esto va a poblar el postcount de categorydto

    @Mapping(target="postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDTO toDTO(Category category);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (posts == null) {
            return 0;
        }
        //contar solo los posts que estan publicados
        return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).count();
    }
}
