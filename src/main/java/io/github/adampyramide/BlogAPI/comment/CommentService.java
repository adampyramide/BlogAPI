package io.github.adampyramide.BlogAPI.comment;

import io.github.adampyramide.BlogAPI.blogpost.BlogPostQueryService;
import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.UserAssembler;
import io.github.adampyramide.BlogAPI.user.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repo;
    private final CommentMapper mapper;

    private final UserAssembler userAssembler;
    private final BlogPostQueryService blogPostQueryService;
    private final SecurityUtils securityUtils;

    // ====================
    // Public methods
    // ====================

    public CommentResponse getCommentById(Long id) {
        return toResponse(getCommentOrThrow(id));
    }

    public Page<CommentResponse> getCommentsByPostId(Long postId, Pageable pageable) {
        return repo.findAllByPostId(postId, pageable).map(this::toResponse);
    }

    public Page<CommentResponse> getCommentsByAuthorId(Long userId, Pageable pageable) {
        return repo.findAllByAuthor_Id(userId, pageable).map(this::toResponse);
    }

    public void updateCommentById(Long id, CommentRequest commentRequest) {
        Comment comment = getCommentOrThrow(id);

        UserUtils.validateOwnership(
                comment.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                "comment"
        );

        mapper.updateEntity(commentRequest, comment);
        repo.save(comment);
    }

    public void deleteCommentById(Long id) {
        Comment comment = getCommentOrThrow(id);

        UserUtils.validateOwnership(
                comment.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                "comment"
        );

        repo.deleteById(id);
    }

    public void createComment(Long postId, CommentRequest commentRequest) {
        Comment comment = mapper.toEntity(commentRequest);
        comment.setAuthor(securityUtils.getAuthenticatedUser());
        comment.setPost(blogPostQueryService.getByIdOrThrow(postId));

        repo.save(comment);
    }

    // ====================
    // Private methods
    // ====================

    private Comment getCommentOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "COMMENT_NOT_FOUND",
                        "Comment with ID %s not found".formatted(id)
                ));
    }

    private CommentResponse toResponse(Comment comment) {
        CommentResponse commentResponse = mapper.toResponse(comment);
        commentResponse.setHasReplies(repo.existsByParentCommentId(comment.getId()));
        userAssembler.enrichUserResponse(comment.getAuthor(), commentResponse.getAuthor());
        return commentResponse;
    }

}
