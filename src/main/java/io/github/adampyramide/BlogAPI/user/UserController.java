package io.github.adampyramide.BlogAPI.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody AuthUserDTO userDTO) {
        service.registerUser(userDTO);
        return service.loginUser(userDTO);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody AuthUserDTO userDTO) {
        return service.loginUser(userDTO);
    }

}
