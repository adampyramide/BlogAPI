package io.github.adampyramide.BlogAPI.comment.dto;

import io.github.adampyramide.BlogAPI.user.dto.UserPreviewResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;
    private Long postId;
    private UserPreviewResponse author;
    private Long parentCommentId;
    private String body;
    private boolean hasReplies;

}