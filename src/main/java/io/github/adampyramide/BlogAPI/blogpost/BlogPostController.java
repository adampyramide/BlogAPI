package io.github.adampyramide.BlogAPI.blogpost;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Blog Posts")
@RestController
@RequestMapping("/api/blog-posts")
public class BlogPostController {

    private final BlogPostService service;

    public BlogPostController(BlogPostService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all existing blogposts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Existing blogposts returned")
            }
    )
    @GetMapping
    public ResponseEntity<List<BlogPostResponseDTO>> getBlogPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getPosts());
    }

    @Operation(
            summary = "Create a new blogpost",
            responses = {
                    @ApiResponse(responseCode = "200", description = "New blogpost created")
            }
    )
    @PostMapping
    public ResponseEntity<Void> createBlogPost(@Valid @RequestBody BlogPostRequestDTO blogPostDTO) {
        service.createPost(blogPostDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Edit an existing blogpost made by the same user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Existing blogpost edited"),
                    @ApiResponse(responseCode = "204", description = "Blogpost not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> editBlogPost(@PathVariable Long id, @Valid @RequestBody BlogPostRequestDTO blogPostDTO) {
        service.editPost(id, blogPostDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Delete an existing blogpost made by the same user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Existing blogpost edited"),
                    @ApiResponse(responseCode = "204", description = "Blogpost not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable Long id) {
        service.deletePost(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Delete multiple existing blogposts made by the same user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Existing blogposts deleted"),
                    @ApiResponse(responseCode = "204", description = "One of the blogposts not found")
            }
    )
    @DeleteMapping
    public ResponseEntity<Void> bulkDeleteBlogPosts(@RequestParam List<Long> ids) {
        service.bulkDeletePosts(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
