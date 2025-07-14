package io.github.adampyramide.BlogAPI.comment;

import io.github.adampyramide.BlogAPI.blogpost.BlogPost;
import io.github.adampyramide.BlogAPI.blogpost.BlogPostRepository;
import io.github.adampyramide.BlogAPI.blogpost.BlogPostResponseDTO;
import io.github.adampyramide.BlogAPI.blogpost.BlogPostService;
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

    private final BlogPostService blogPostService;

    private final SecurityUtils securityUtils;

    public CommentService(CommentRepository repo, CommentMapper mapper, BlogPostService blogPostService, SecurityUtils securityUtils) {
        this.repo = repo;
        this.mapper = mapper;
        this.blogPostService = blogPostService;
        this.securityUtils = securityUtils;
    }

    public CommentResponseDTO getCommentById(Long id) {
        return mapper.toResponseDTO(
                getCommentOrThrow(id)
        );
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

    public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
        return repo.findAllByPostId(postId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public void createComment(Long postId, CommentRequestDTO commentDTO) {
        Comment comment = mapper.toEntity(commentDTO);
        comment.setAuthor(securityUtils.getAuthenticatedUser());
        comment.setPost(blogPostService.getBlogPostEntityById(postId));

        repo.save(comment);
    }

    public List<CommentResponseDTO> getCommentsByAuthorId(Long userId) {
        return repo.findAllByAuthor_Id(userId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    private Comment getCommentOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));
    }

}
