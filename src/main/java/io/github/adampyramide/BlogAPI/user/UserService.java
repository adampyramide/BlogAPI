package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.exception.ApiRequestException;
import org.springframework.http.HttpStatus;
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

    public void registerUser(UserDTO userDTO) {
        if (repo.existsByUsername(userDTO.username())) {
            throw new ApiRequestException("Username is already taken", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setUsername(userDTO.username());
        user.setPassword(passwordEncoder.encode(userDTO.password()));

        repo.save(user);
    }

}
