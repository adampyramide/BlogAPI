package io.github.adampyramide.BlogAPI.auth;

import io.github.adampyramide.BlogAPI.user.AuthUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "User authorization")

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered and token returned")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody AuthUserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerUser(userDTO));
    }

    @Operation(
            responses = {
                    @ApiResponse(responseCode = "200", description = "Valid credentials passed and token returned"),
                    @ApiResponse(responseCode = "404", description = "Invalid credentials passed", content = @Content())
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthUserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(service.loginUser(userDTO));
    }

}
