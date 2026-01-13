package io.github.adampyramide.BlogAPI.comment;

import io.github.adampyramide.BlogAPI.blogpost.BlogPostQueryService;
import io.github.adampyramide.BlogAPI.comment.dto.CommentRequest;
import io.github.adampyramide.BlogAPI.comment.dto.CommentResponse;
import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service responsible for managing comment operations.
 * <p>
 * Provides methods to create, update, delete, and retrieve comments.
 * Enforces ownership rules via {@link UserUtils} and integrates with
 * {@link BlogPostQueryService} to validate associated blog posts.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private static final String RESOURCE_NAME = Comment.class.getSimpleName();

    private final CommentRepository repo;
    private final CommentMapper mapper;
    private final BlogPostQueryService blogPostQueryService;
    private final SecurityUtils securityUtils;

    /**
     * Retrieves a comment by its ID.
     *
     * @param id the ID of the comment
     * @return the comment as a {@link CommentResponse}
     * @throws ApiException if the comment does not exist
     */
    public CommentResponse getCommentById(Long id) {
        return toResponse(getCommentOrThrow(id));
    }

    /**
     * Retrieves all comments for a given blog post, paginated.
     *
     * @param postId the ID of the blog post
     * @param pageable pagination information
     * @return a page of {@link CommentResponse} objects
     */
    public Page<CommentResponse> getCommentsByPostId(Long postId, Pageable pageable) {
        return repo.findAllByPostId(postId, pageable).map(this::toResponse);
    }

    /**
     * Retrieves all comments authored by a specific user, paginated.
     *
     * @param userId the ID of the author
     * @param pageable pagination information
     * @return a page of {@link CommentResponse} objects
     */
    public Page<CommentResponse> getCommentsByAuthorId(Long userId, Pageable pageable) {
        return repo.findAllByAuthorId(userId, pageable).map(this::toResponse);
    }

    /**
     * Updates a comment by its ID.
     * <p>
     * Validates that the authenticated user is the owner of the comment.
     *
     * @param id the ID of the comment to update
     * @param commentRequest the updated comment data
     * @return the updated comment as a {@link CommentResponse}
     * @throws ApiException if the comment does not exist or user is not the owner
     */
    public CommentResponse updateCommentById(Long id, CommentRequest commentRequest) {
        Comment comment = getCommentOrThrow(id);

        UserUtils.validateOwnership(
                comment.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                RESOURCE_NAME
        );

        mapper.updateEntity(commentRequest, comment);
        repo.save(comment);

        return toResponse(comment);
    }

    /**
     * Creates a new comment on a specified blog post.
     * <p>
     * Sets the authenticated user as the author and validates the blog post exists.
     *
     * @param postId the ID of the blog post to comment on
     * @param commentRequest the comment data
     * @return the created comment as a {@link CommentResponse}
     * @throws ApiException if the blog post does not exist
     */
    public CommentResponse createComment(Long postId, CommentRequest commentRequest) {
        Comment comment = mapper.toEntity(commentRequest);
        comment.setAuthor(securityUtils.getAuthenticatedUser());
        comment.setPost(blogPostQueryService.getByIdOrThrow(postId));

        repo.save(comment);

        return toResponse(comment);
    }

    /**
     * Deletes a comment by its ID.
     * <p>
     * Validates that the authenticated user is the owner of the comment.
     *
     * @param id the ID of the comment to delete
     * @throws ApiException if the comment does not exist or user is not the owner
     */
    public void deleteCommentById(Long id) {
        Comment comment = getCommentOrThrow(id);

        UserUtils.validateOwnership(
                comment.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                RESOURCE_NAME
        );

        repo.deleteById(id);
    }

    /**
     * Returns the comment with the given ID or throws an exception if not found.
     */
    private Comment getCommentOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "COMMENT_NOT_FOUND",
                        "Comment with ID %s not found".formatted(id)
                ));
    }

    /**
     * Maps a Comment entity to a CommentResponse and sets the hasReplies flag.
     */
    private CommentResponse toResponse(Comment comment) {
        CommentResponse commentResponse = mapper.toResponse(comment);
        commentResponse.setHasReplies(repo.existsByParentCommentId(comment.getId()));
        return commentResponse;
    }

}
