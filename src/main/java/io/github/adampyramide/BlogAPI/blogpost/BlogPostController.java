package io.github.adampyramide.BlogAPI.blogpost;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Blogposts")
@SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/api")
public class BlogPostController {

    private final BlogPostService service;

    public BlogPostController(BlogPostService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all blogposts",
            description = "Returns a list of all blogposts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of all blogposts")
            }
    )
    @GetMapping("/blog-posts")
    public ResponseEntity<List<BlogPostResponseDTO>> getBlogPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getBlogPosts());
    }

    @Operation(
            summary = "Get blogpost",
            description = "Returns a blogpost with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Blogpost returned"),
                    @ApiResponse(responseCode = "404", description = "Blogpost not found", content = @Content())
            }
    )
    @GetMapping("/blog-posts/{id}")
    public ResponseEntity<BlogPostResponseDTO> getBlogPostById(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getBlogPostById(postId));
    }

    @Operation(
            summary = "Create blogpost",
            description = "Creates a blogpost",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Blogpost created")
            }
    )
    @PostMapping("/blog-posts")
    public ResponseEntity<Void> createBlogPost(@Valid @RequestBody BlogPostRequestDTO blogPostDTO) {
        service.createBlogPost(blogPostDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Edit blogpost",
            description = "Edits a blogpost with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Blogpost edited"),
                    @ApiResponse(responseCode = "404", description = "Blogpost not found")
            }
    )
    @PutMapping("/blog-posts/{id}")
    public ResponseEntity<Void> editBlogPostById(@PathVariable Long postId, @Valid @RequestBody BlogPostRequestDTO blogPostDTO) {
        service.editBlogPostById(postId, blogPostDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Delete blogpost",
            description = "Deletes a blogpost with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Blogpost deleted"),
                    @ApiResponse(responseCode = "404", description = "Blogpost not found")
            }
    )
    @DeleteMapping("/blog-posts/{id}")
    public ResponseEntity<Void> deleteBlogPostById(@PathVariable Long postId) {
        service.deleteBlogPostById(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Bulk delete blogposts",
            description = "Deletes multiple blogposts with the specified IDs",
            parameters = {
                    @Parameter(
                            name = "ids",
                            description = "Comma-separated list of blogpost IDs",
                            required = true,
                            in = ParameterIn.QUERY
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "All blogposts deleted"),
                    @ApiResponse(responseCode = "404", description = "One or more blogposts not found")
            }
    )
    @DeleteMapping("/blog-posts")
    public ResponseEntity<Void> bulkDeleteBlogPosts(@RequestParam List<Long> ids) {
        service.bulkDeletePostsByIds(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Get all blogposts by user",
            description = "Get all the blogposts created by the specified user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of blogposts created by the user"),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content())
            }
    )
    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<BlogPostResponseDTO>> getBlogPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getBlogPostsByUserId(userId));
    }

}
