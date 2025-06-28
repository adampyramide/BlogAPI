package io.github.adampyramide.BlogAPI.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/api/auth/register")
    public String registerUser(@RequestBody UserDTO userDTO) {
        service.registerUser(userDTO);
        return service.loginUser(userDTO);
    }

    @PostMapping("/api/auth/login")
    public String loginUser(@RequestBody UserDTO userDTO) {
        return service.loginUser(userDTO);
    }

    @GetMapping("/api/closed-endpoint")
    public String closedEndPoint() {
        System.out.println("Closed end point ran");
        return "Closed end point ran";
    }

}
