package io.github.adampyramide.BlogAPI.reaction;

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

@Tag(name = "Reactions")
@SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/api/blog-posts/{postId}/reactions")
public class ReactionController {

    final private ReactionService service;

    public ReactionController(ReactionService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get reactions for blogpost",
            description = "Returns a paginated list of reactions (users and their reaction type) for the specified blogpost. Optional filter by reaction type (LIKE or DISLIKE). Sorted by author ID descending.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users and their reaction type returned"),
                    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content())
            }
    )
    @GetMapping
    public ResponseEntity<Page<ReactionResponseDTO>> getReactionsByPostId(
            @PathVariable Long postId,
            @RequestParam(required = false) ReactionType reactionType,
            //POSSIBLY CHANGE SORT TO A CREATIONTIME
            @PageableDefault(size = 10, sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.getReactionsByPostId(postId, reactionType, pageable));
    }

    @Operation(
            summary = "Add reaction to blogpost",
            description = "Adds a reaction (LIKE or DISLIKE) to a blogpost by the current user. Overwrites users existing reaction if it exists.",
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
            summary = "Remove reaction from blogpost",
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
