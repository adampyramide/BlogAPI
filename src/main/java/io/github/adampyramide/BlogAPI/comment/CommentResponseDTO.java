package io.github.adampyramide.BlogAPI.comment;

import io.github.adampyramide.BlogAPI.user.PublicUserDTO;

public record CommentResponseDTO(

        long id,
        long postId,
        Long parentCommentId,
        PublicUserDTO author,
        String body
        
) {}
