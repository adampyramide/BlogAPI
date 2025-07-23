package io.github.adampyramide.BlogAPI.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users")
@SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(
            summary = "Delete user by session",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @DeleteMapping
    public ResponseEntity<PublicUserDTO> deleteUser() {
        service.deleteUser();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Get user by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User returned"),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content())
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PublicUserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getUserById(id));
    }

}
