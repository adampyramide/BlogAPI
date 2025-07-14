package io.github.adampyramide.BlogAPI.comment;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/blog-posts/{id}/comments")
    public List<CommentResponseDTO> getCommentsByPostId(@PathVariable long id) {
        return service.getCommentsByPostId(id);
    }

    @PostMapping("/blog-posts/{id}/comments")
    public void createComment(@PathVariable long id, @RequestBody CommentRequestDTO commentDTO) {
        service.createComment(id, commentDTO);
    }

    @GetMapping("comments/{id}")
    public CommentResponseDTO getComment(@PathVariable long id) {
        return service.getComment(id);
    }

    @PutMapping("/comments/{id}")
    public void editComment(@PathVariable long id, @RequestBody CommentRequestDTO commentDTO) {
        service.editComment(id, commentDTO);
    }

    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable long id) {
        service.deleteComment(id);
    }

    @GetMapping("/users/{id}/comments")
    public List<CommentResponseDTO> getCommentsByUserId(@PathVariable Long id) {
        return service.getCommentsByAuthorId(id);
    }

}
