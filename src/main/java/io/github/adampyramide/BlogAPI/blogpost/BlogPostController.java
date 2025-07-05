package io.github.adampyramide.BlogAPI.blogpost;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog-posts")
public class BlogPostController {

    private final BlogPostService service;

    public BlogPostController(BlogPostService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BlogPostResponseDTO>> getBlogPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getPosts());
    }

    @PostMapping
    public ResponseEntity<Void> createBlogPost(@Valid @RequestBody BlogPostRequestDTO blogPostDTO) {
        service.createPost(blogPostDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editBlogPost(@PathVariable int id, @Valid @RequestBody BlogPostRequestDTO blogPostDTO) {
        service.editPost(id, blogPostDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable int id) {
        service.deletePost(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> bulkDeleteBlogPosts(@RequestParam List<Integer> ids) {
        service.bulkDeletePosts(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
