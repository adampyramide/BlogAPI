package io.github.adampyramide.BlogAPI.comment;

import io.github.adampyramide.BlogAPI.blogpost.BlogPost;
import io.github.adampyramide.BlogAPI.blogpost.BlogPostRepository;
import io.github.adampyramide.BlogAPI.blogpost.BlogPostResponseDTO;
import io.github.adampyramide.BlogAPI.exception.CustomException;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository repo;
    private final CommentMapper mapper;

    private final BlogPostRepository postRepo;

    private final SecurityUtils securityUtils;

    public CommentService(CommentRepository repo, CommentMapper mapper, BlogPostRepository postRepo, SecurityUtils securityUtils) {
        this.repo = repo;
        this.mapper = mapper;
        this.postRepo = postRepo;
        this.securityUtils = securityUtils;
    }

    public CommentResponseDTO getComment(long id) {
        return mapper.toResponseDTO(
                repo.findById(id)
                        .orElseThrow(() -> new CustomException("Comment not found", HttpStatus.NOT_FOUND))
        );
    }

    public void createComment(long postId, CommentRequestDTO commentDTO) {
        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new CustomException("Post not found", HttpStatus.NOT_FOUND));

        Comment comment = mapper.toEntity(commentDTO);
        comment.setAuthor(securityUtils.getAuthenticatedUser());
        comment.setPost(post);

        repo.save(comment);

    }

    public void editComment(long id, CommentRequestDTO commentDTO) {
        Comment comment = repo.findById(id)
                .orElseThrow(() -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));

        checkAuthorOrThrow(comment);
        mapper.updateEntityWithDto(commentDTO, comment);
        repo.save(comment);
    }

    public void deleteComment(long id) {
        Comment comment = repo.findById(id)
                .orElseThrow(() -> new CustomException("Comment not found", HttpStatus.NOT_FOUND));

        checkAuthorOrThrow(comment);
        repo.deleteById(id);
    }

    public List<CommentResponseDTO> getCommentsByPostId(long postId) {
        return repo.findAllByPostId(postId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public List<CommentResponseDTO> getCommentsByAuthorId(long userId) {
        return repo.findAllByAuthor_Id(userId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public void checkAuthorOrThrow(Comment comment) {
        if (comment.getAuthor().getId() != securityUtils.getAuthenticatedUser().getId())
            throw new CustomException("Comment not made by user", HttpStatus.UNAUTHORIZED);
    }

}
