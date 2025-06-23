package io.github.adampyramide.BlogAPI.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterUserDTO registerUserDTO) {
        if (repo.existsByUsername(registerUserDTO.username())) {
            throw new IllegalArgumentException("Username already in use");
        }

        User user = new User();
        user.setUsername(registerUserDTO.username());
        user.setPassword(passwordEncoder.encode(registerUserDTO.password()));

        repo.save(user);
    }

}
