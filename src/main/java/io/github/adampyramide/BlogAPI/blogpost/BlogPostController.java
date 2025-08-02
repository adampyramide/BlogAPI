package io.github.adampyramide.BlogAPI.blogpost;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Blogposts")
@SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService service;

    @Operation(
            summary = "Get all blog posts",
            description = "Returns a paginated list of all blog posts. Sorted by creation time descending.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of all blog posts")
            }
    )
    @GetMapping("/blog-posts")
    public ResponseEntity<Page<BlogPostResponse>> getBlogPosts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.getBlogPosts(pageable));
    }

    @Operation(
            summary = "Create blog post",
            description = "Creates a blog post",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Blog post created")
            }
    )
    @PostMapping("/blog-posts")
    public ResponseEntity<BlogPostResponse> createBlogPost(@Valid @RequestBody CreateBlogPostRequest blogPostRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                service.createBlogPost(blogPostRequest)
        );
    }

    @Operation(
            summary = "Get blog post",
            description = "Returns a blog post with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Blog post returned"),
                    @ApiResponse(responseCode = "404", description = "Blog post not found", content = @Content())
            }
    )
    @GetMapping("/blog-posts/{postId}")
    public ResponseEntity<BlogPostResponse> getBlogPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(service.getBlogPostById(postId));
    }

    @Operation(
            summary = "Update blog post",
            description = "Update a blog post with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Blog post edited"),
                    @ApiResponse(responseCode = "404", description = "Blog post not found")
            }
    )
    @PatchMapping("/blog-posts/{postId}")
    public ResponseEntity<BlogPostResponse> updateBlogPostById(@PathVariable Long postId, @Valid @RequestBody UpdateBlogPostRequest blogPostRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(
                service.updateBlogPostById(postId, blogPostRequest)
        );
    }

    @Operation(
            summary = "Delete blog post",
            description = "Deletes a blog post with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Blog post deleted"),
                    @ApiResponse(responseCode = "404", description = "Blog post not found")
            }
    )
    @DeleteMapping("/blog-posts/{postId}")
    public ResponseEntity<Void> deleteBlogPostById(@PathVariable Long postId) {
        service.deleteBlogPostById(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Bulk delete blog posts",
            description = "Deletes multiple blog posts with the specified IDs",
            parameters = {
                    @Parameter(
                            name = "ids",
                            description = "Comma-separated list of blog post IDs",
                            required = true,
                            in = ParameterIn.QUERY
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "All blog posts deleted"),
                    @ApiResponse(responseCode = "404", description = "One or more blog posts not found")
            }
    )
    @DeleteMapping("/blog-posts")
    public ResponseEntity<Void> bulkDeleteBlogPostsByIds(@RequestParam List<Long> ids) {
        service.bulkDeleteBlogPostsByIds(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Get all blog posts by user",
            description = "Returns a paginated list of blog posts created by the specified user. Sorted by creation time descending.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of blog posts created by the user returned"),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content())
            }
    )
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<Page<BlogPostResponse>> getBlogPostsByUserId(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.getBlogPostsByUserId(userId, pageable));
    }

}
