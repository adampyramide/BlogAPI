package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.blogpost.BlogPostResponseDTO;
import io.github.adampyramide.BlogAPI.blogpost.BlogPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    private final BlogPostService blogPostService;

    public UserController(UserService service, BlogPostService blogPostService) {
        this.service = service;
        this.blogPostService = blogPostService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicUserDTO> getUserById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getUserById(id));
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<BlogPostResponseDTO>> getPostsByUserId(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(blogPostService.getPostsByUserId(id));
    }

}
