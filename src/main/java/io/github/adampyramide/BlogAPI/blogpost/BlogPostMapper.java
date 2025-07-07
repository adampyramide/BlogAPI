package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BlogPostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "author", ignore = true)
    BlogPost toEntity(BlogPostRequestDTO dto);

    BlogPostResponseDTO toResponseDTO(BlogPost blogPost);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateBlogPostFromDto(BlogPostRequestDTO dto, @MappingTarget BlogPost entity);

}
