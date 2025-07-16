package io.github.adampyramide.BlogAPI.comment;

import io.github.adampyramide.BlogAPI.blogpost.BlogPostValidator;
import io.github.adampyramide.BlogAPI.exception.CustomException;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.util.OwnershipValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository repo;
    private final CommentMapper mapper;

    private final BlogPostValidator blogPostValidator;

    private final SecurityUtils securityUtils;

    public CommentService(CommentRepository repo, CommentMapper mapper, BlogPostValidator blogPostValidator, SecurityUtils securityUtils) {
        this.repo = repo;
        this.mapper = mapper;
        this.blogPostValidator = blogPostValidator;
        this.securityUtils = securityUtils;
    }

    // ====================
    // Public methods
    // ====================

    public CommentResponseDTO getCommentById(Long id) {
        return toResponseDTO(getCommentOrThrow(id));
    }

    public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
        return repo.findAllByPostId(postId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<CommentResponseDTO> getCommentsByAuthorId(Long userId) {
        return repo.findAllByAuthor_Id(userId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public void editCommentById(Long id, CommentRequestDTO commentDTO) {
        Comment comment = getCommentOrThrow(id);

        OwnershipValidator.authorizeAuthor(
                comment.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                "comment"
        );

        mapper.updateEntityWithDto(commentDTO, comment);
        repo.save(comment);
    }

    public void deleteCommentById(Long id) {
        Comment comment = getCommentOrThrow(id);

        OwnershipValidator.authorizeAuthor(
                comment.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                "comment"
        );

        repo.deleteById(id);
    }

    public void createComment(Long postId, CommentRequestDTO commentDTO) {
        Comment comment = mapper.toEntity(commentDTO);
        comment.setAuthor(securityUtils.getAuthenticatedUser());
        comment.setPost(blogPostValidator.getByIdOrThrow(postId));

        repo.save(comment);
    }

    // ====================
    // Private methods
    // ====================

    private Comment getCommentOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));
    }

    private CommentResponseDTO toResponseDTO(Comment comment) {
        CommentResponseDTO dto = mapper.toResponseDTO(comment);
        dto.setHasReplies(repo.existsByParentCommentId(comment.getId()));
        return dto;
    }


}
