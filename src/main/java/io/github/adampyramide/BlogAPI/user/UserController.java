package io.github.adampyramide.BlogAPI.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
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
