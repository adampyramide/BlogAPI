package io.github.adampyramide.BlogAPI.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
            parameters = {
                    @Parameter(name = "id", description = "ID of the comment", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment returned"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            }
    )
    @GetMapping("comments/{id}")
    public CommentResponseDTO getCommentById(@PathVariable Long id) {
        return service.getCommentById(id);
    }

    @Operation(
            summary = "Edit comment",
            description = "Edits a comment with the specified ID",
            parameters = {
                    @Parameter(name = "id", description = "ID of the comment", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment edited"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            }
    )
    @PutMapping("/comments/{id}")
    public void editCommentById(@PathVariable Long id, @RequestBody CommentRequestDTO commentDTO) {
        service.editCommentById(id, commentDTO);
    }

    @Operation(
            summary = "Delete comment",
            description = "Deletes a comment with the specified ID",
            parameters = {
                    @Parameter(name = "id", description = "ID of the comment", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment deleted"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            }
    )
    @DeleteMapping("/comments/{id}")
    public void deleteCommentById(@PathVariable Long id) {
        service.deleteCommentById(id);
    }

    @Operation(
            summary = "Get comments for blogpost",
            description = "Returns a list of all comments created on a certain blogpost",
            parameters = {
                    @Parameter(name = "id", description = "ID of the blogpost", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments on blogpost returned"),
                    @ApiResponse(responseCode = "404", description = "Blogpost not found")
            }
    )
    @GetMapping("/blog-posts/{id}/comments")
    public List<CommentResponseDTO> getCommentsByPostId(@PathVariable Long id) {
        return service.getCommentsByPostId(id);
    }

    @Operation(
            summary = "Create comment",
            description = "Creates a comment on the specified blog post ID",
            parameters = {
                    @Parameter(name = "id", description = "ID of the blogpost", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments created"),
                    @ApiResponse(responseCode = "404", description = "Blogpost not found")
            }
    )
    @PostMapping("/blog-posts/{id}/comments")
    public void createComment(@PathVariable Long id, @RequestBody CommentRequestDTO commentDTO) {
        service.createComment(id, commentDTO);
    }

    @Operation(
            summary = "Get all comments by user",
            description = "Returns a list of all comments created by a certain user",
            parameters = {
                    @Parameter(name = "id", description = "ID of the user", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments by user returned"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("/users/{id}/comments")
    public List<CommentResponseDTO> getCommentsByAuthorId(@PathVariable Long id) {
        return service.getCommentsByAuthorId(id);
    }

}
