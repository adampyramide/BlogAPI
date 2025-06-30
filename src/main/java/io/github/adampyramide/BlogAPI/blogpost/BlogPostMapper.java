package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BlogPostMapper {

    BlogPost toEntity(BlogPostRequestDTO dto);

    BlogPostResponseDTO toResponseDTO(BlogPost blogPost);

    void updateBlogPostFromDto(BlogPostRequestDTO dto, @MappingTarget BlogPost entity);

}
