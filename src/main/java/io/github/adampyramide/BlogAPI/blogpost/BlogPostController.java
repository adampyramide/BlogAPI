package io.github.adampyramide.BlogAPI.blogpost;

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
    public List<BlogPostResponseDTO> getBlogPosts() {
        return service.getPosts();
    }

    @PostMapping
    public void createBlogPost(@RequestBody BlogPostRequestDTO blogPostDTO) {
        service.createPost(blogPostDTO);
    }

    @PutMapping("/{id}")
    public void editBlogPost(@PathVariable int id, @RequestBody BlogPostRequestDTO blogPostDTO) {
        service.editPost(id, blogPostDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable int id) {
        service.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
