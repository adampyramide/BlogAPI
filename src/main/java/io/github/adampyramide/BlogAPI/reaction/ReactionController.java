package io.github.adampyramide.BlogAPI.reaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog-posts/{postId}/reactions")
public class ReactionController {

    private ReactionService service;

    public ReactionController(ReactionService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get reactions for blogpost",
            description = "Returns a list of users and their reaction type for the specified blogpost. Optional filter by reaction type (LIKE or DISLIKE).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users and their reaction type returned"),
                    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content())
            }
    )
    @GetMapping
    public ResponseEntity<List<ReactionResponseDTO>> getReactionsByPostId(@PathVariable Long postId, @RequestParam(required = false) ReactionType reactionType) {
        return ResponseEntity.ok(service.getReactionsByPostId(postId, reactionType));
    }

    @Operation(
            summary = "Add reaction",
            description = "Adds a reaction (LIKE or DISLIKE) to a blog post by the current user. Overwrites any existing reaction.",
    responses = {
                    @ApiResponse(responseCode = "201", description = "Reaction added to blogpost"),
                    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content())
            }
    )
    @PostMapping
    public ResponseEntity<Void> addReactionByPostId(@PathVariable Long postId, @RequestBody ReactionRequestDTO reactionDTO) {
        service.addReactionByPostId(postId, reactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Remove reaction",
            description = "Removes the current user's reaction (LIKE or DISLIKE) from the blog post.",
    responses = {
                    @ApiResponse(responseCode = "204", description = "Reaction removed from blogpost"),
                    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content())
            }
    )
    @DeleteMapping
    public ResponseEntity<Void> deleteReactionByPostId(@PathVariable Long postId) {
        service.deleteReactionByPostId(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
