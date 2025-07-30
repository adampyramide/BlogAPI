package io.github.adampyramide.BlogAPI.reaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ReactionController {

    final private ReactionService service;

    @Operation(
            summary = "Get reactions for blog post",
            description = "Returns a paginated list of reactions (users and their reaction type) for the specified blog post. Optional filter by reaction type (LIKE or DISLIKE). Sorted by author ID descending.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users and their reaction type returned"),
                    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content())
            }
    )
    @GetMapping
    public ResponseEntity<Page<ReactionResponse>> getReactionsByPostId(
            @PathVariable Long postId,
            @RequestParam(required = false) ReactionType reactionType,
            @PageableDefault(size = 10, sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.getReactionsByPostId(postId, reactionType, pageable));
    }

    @Operation(
            summary = "Add reaction to blog post",
            description = "Adds a reaction (LIKE or DISLIKE) to a blog post by the current user. Overwrites users existing reaction if it exists.",
    responses = {
                    @ApiResponse(responseCode = "201", description = "Reaction added to blog post"),
                    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content())
            }
    )
    @PostMapping
    public ResponseEntity<Void> addReactionByPostId(@PathVariable Long postId, @RequestBody ReactionRequest reactionRequest) {
        service.addReactionByPostId(postId, reactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Remove reaction from blog post",
            description = "Removes the current user's reaction (LIKE or DISLIKE) from the blog post.",
    responses = {
                    @ApiResponse(responseCode = "204", description = "Reaction removed from blog post"),
                    @ApiResponse(responseCode = "404", description = "Post not found", content = @Content())
            }
    )
    @DeleteMapping
    public ResponseEntity<Void> deleteReactionByPostId(@PathVariable Long postId) {
        service.deleteReactionByPostId(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
