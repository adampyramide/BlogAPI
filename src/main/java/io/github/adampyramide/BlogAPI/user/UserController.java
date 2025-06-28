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

    @PostMapping("/api/public/user/register")
    public String registerUser(@RequestBody UserDTO userDTO) {
        service.registerUser(userDTO);
        return service.loginUser(userDTO);
    }

    @PostMapping("/api/public/user/login")
    public String loginUser(@RequestBody UserDTO userDTO) {
        return service.loginUser(userDTO);
    }

    @GetMapping("/api/admin")
    public String admin() {
        System.out.println("Running as admin");
        return "Running as admin";
    }

}
