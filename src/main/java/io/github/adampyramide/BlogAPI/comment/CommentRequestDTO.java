package io.github.adampyramide.BlogAPI.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequestDTO(

        @NotBlank(message = "Comment must not be blank")
        @Size(max = 300, message = "Comment must be under 300 characters")
        String body,

        Long parentCommentId

) {}
