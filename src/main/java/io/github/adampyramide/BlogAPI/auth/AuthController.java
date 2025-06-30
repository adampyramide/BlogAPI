package io.github.adampyramide.BlogAPI.auth;

import io.github.adampyramide.BlogAPI.user.AuthUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthUserDTO userDTO) {
        service.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.loginUser(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthUserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(service.loginUser(userDTO));
    }

}
