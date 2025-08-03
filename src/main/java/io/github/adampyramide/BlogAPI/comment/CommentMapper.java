package io.github.adampyramide.BlogAPI.comment;

import io.github.adampyramide.BlogAPI.comment.dto.CommentRequest;
import io.github.adampyramide.BlogAPI.comment.dto.CommentResponse;
import io.github.adampyramide.BlogAPI.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = UserMapper.class
)
public interface CommentMapper {

    Comment toEntity(CommentRequest requestDTO);

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "parentComment.id", target = "parentCommentId")
    CommentResponse toResponse(Comment entity);

    void updateEntity(CommentRequest dto, @MappingTarget Comment entity);

}