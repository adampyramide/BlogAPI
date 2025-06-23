package io.github.adampyramide.BlogAPI.user;

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
    public void registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        service.registerUser(registerUserDTO);
    }

}
