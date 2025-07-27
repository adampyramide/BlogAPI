package io.github.adampyramide.BlogAPI.comment;

import io.github.adampyramide.BlogAPI.user.PublicUserResponse;
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
    private Long parentCommentId;
    private PublicUserResponse author;
    private String body;
    private boolean hasReplies;

}