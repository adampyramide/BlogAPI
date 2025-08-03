package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.user.dto.PublicUserResponse;
import io.github.adampyramide.BlogAPI.user.dto.UpdateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users")
@SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(
            summary = "Delete user by session",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted")
            }
    )
    @DeleteMapping("/me")
    public ResponseEntity<PublicUserResponse> deleteUser() {
        service.deleteUser();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Update user information by session",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User information updated")
            }
    )
    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PublicUserResponse> updateUser(@ModelAttribute UpdateUserRequest userRequest) {
        service.updateUser(userRequest);
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
    public ResponseEntity<PublicUserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getUserById(id));
    }

}
