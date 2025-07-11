package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.blogpost.BlogPostResponseDTO;
import io.github.adampyramide.BlogAPI.blogpost.BlogPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "User-specific operations")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    private final BlogPostService blogPostService;

    public UserController(UserService service, BlogPostService blogPostService) {
        this.service = service;
        this.blogPostService = blogPostService;
    }

    @Operation(
            summary = "Get a user by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PublicUserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getUserById(id));
    }

    @Operation(
            summary = "Get all posts created by a user with specified id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("/{id}/posts")
    public ResponseEntity<List<BlogPostResponseDTO>> getPostsByUserId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(blogPostService.getPostsByUserId(id));
    }

}
