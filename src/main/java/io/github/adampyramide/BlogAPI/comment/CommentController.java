package io.github.adampyramide.BlogAPI.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comments")
@SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/api")
public class CommentController {

    final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get comment",
            description = "Returns a comment with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment returned"),
                    @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content())
            }
    )
    @GetMapping("comments/{id}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCommentById(commentId));
    }

    @Operation(
            summary = "Edit comment",
            description = "Edits a comment with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment edited"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            }
    )
    @PutMapping("/comments/{id}")
    public ResponseEntity<Void> editCommentById(@PathVariable Long commentId, @RequestBody CommentRequestDTO commentDTO) {
        service.editCommentById(commentId, commentDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Delete comment",
            description = "Deletes a comment with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Comment deleted"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            }
    )
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long commentId) {
        service.deleteCommentById(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Get comments for blogpost",
            description = "Returns a list of all comments created on a certain blogpost",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments on blogpost returned"),
                    @ApiResponse(responseCode = "404", description = "Blogpost not found", content = @Content())
            }
    )
    @GetMapping("/blog-posts/{id}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCommentsByPostId(postId));
    }

    @Operation(
            summary = "Create comment",
            description = "Creates a comment on the specified blog post ID",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Comments created"),
                    @ApiResponse(responseCode = "404", description = "Blogpost not found")
            }
    )
    @PostMapping("/blog-posts/{id}/comments")
    public ResponseEntity<Void> createComment(@PathVariable Long postId, @RequestBody CommentRequestDTO commentDTO) {
        service.createComment(postId, commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get all comments by user",
            description = "Returns a list of all comments created by a certain user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments by user returned"),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content())
            }
    )
    @GetMapping("/users/{id}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByAuthorId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCommentsByAuthorId(userId));
    }

}
