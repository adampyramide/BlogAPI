package io.github.adampyramide.BlogAPI.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    @GetMapping("comments/{commentId}")
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
    @PutMapping("/comments/{commentId}")
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
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long commentId) {
        service.deleteCommentById(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Get comments for blogpost",
            description = "Returns a list of all comments created on the specified blogpost",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments on blogpost returned"),
                    @ApiResponse(responseCode = "404", description = "Blogpost not found", content = @Content())
            }
    )
    @GetMapping("/blog-posts/{postId}/comments")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByPostId(
            @PathVariable Long postId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.getCommentsByPostId(postId, pageable));
    }

    @Operation(
            summary = "Create comment on blogpost",
            description = "Creates a comment on the specified blogpost",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Comments created"),
                    @ApiResponse(responseCode = "404", description = "Blogpost not found")
            }
    )
    @PostMapping("/blog-posts/{postId}/comments")
    public ResponseEntity<Void> createComment(@PathVariable Long postId, @RequestBody CommentRequestDTO commentDTO) {
        service.createComment(postId, commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get all comments by user",
            description = "Returns a list of all comments created by the specified user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments by user returned"),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content())
            }
    )
    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByAuthorId(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.getCommentsByAuthorId(userId, pageable));
    }

}
