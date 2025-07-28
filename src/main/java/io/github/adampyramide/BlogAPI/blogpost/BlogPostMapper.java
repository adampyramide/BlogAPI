package io.github.adampyramide.BlogAPI.blogpost;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BlogPostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "author", ignore = true)
    BlogPost toEntity(CreateBlogPostRequest request);

    BlogPostResponse toResponse(BlogPost entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateEntity(UpdateBlogPostRequest request, @MappingTarget BlogPost entity);

}
