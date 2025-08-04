package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.user.dto.UserPreviewResponse;
import io.github.adampyramide.BlogAPI.user.dto.UpdateUserRequest;
import io.github.adampyramide.BlogAPI.user.dto.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Users")
@SecurityRequirement(name = "bearerAuth")

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(
            summary = "Delete authenticated user",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted")
            }
    )
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser() {
        service.deleteUser();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Update authenticated user's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User information updated")
            }
    )
    @PatchMapping(value = "/me")
    public ResponseEntity<UserProfileResponse> updateUser(@Valid @RequestBody UpdateUserRequest userRequest) {
        return ResponseEntity.ok(service.updateUser(userRequest));
    }

    @Operation(
            summary = "Update authenticated user's avatar",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User avatar updated")
            }
    )
    @PostMapping(value = "/me/avatar")
    public ResponseEntity<UserPreviewResponse> updateUserAvatar(@RequestParam("avatarImage") MultipartFile avatarImage) {
        return ResponseEntity.ok(service.updateUserAvatar(avatarImage));
    }

    @Operation(
            summary = "Delete authenticated user's avatar",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User avatar updated")
            }
    )
    @DeleteMapping(value = "/me/avatar")
    public ResponseEntity<Void> deleteUserAvatar() {
        service.deleteUserAvatar();
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
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getUserById(id));
    }

}
