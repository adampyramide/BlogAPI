package io.github.adampyramide.BlogAPI.comment;

import io.github.adampyramide.BlogAPI.user.PublicUserDTO;

public record CommentResponseDTO(

        Long id,
        Long postId,
        Long parentCommentId,
        PublicUserDTO author,
        String body
        
) {}
