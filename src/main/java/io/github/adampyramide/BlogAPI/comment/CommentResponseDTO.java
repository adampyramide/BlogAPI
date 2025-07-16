package io.github.adampyramide.BlogAPI.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.adampyramide.BlogAPI.user.PublicUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {

    private Long id;
    private Long postId;
    private Long parentCommentId;
    private PublicUserDTO author;
    private String body;
    private boolean hasReplies;

}