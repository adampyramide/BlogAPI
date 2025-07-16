package io.github.adampyramide.BlogAPI.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toEntity(CommentRequestDTO requestDTO);

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "parentComment.id", target = "parentCommentId")
    CommentResponseDTO toResponseDTO(Comment entity);

    void updateEntityWithDto(CommentRequestDTO dto, @MappingTarget Comment entity);

}