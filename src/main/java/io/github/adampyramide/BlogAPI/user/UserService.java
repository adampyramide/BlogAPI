package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.exception.ApiRequestException;
import io.github.adampyramide.BlogAPI.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepository repo, AuthenticationManager authManager, JwtService jwtService) {
        this.repo = repo;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public void registerUser(UserDTO userDTO) {
        if (repo.existsByUsername(userDTO.username()))
            throw new ApiRequestException("Username is already taken", HttpStatus.CONFLICT);

        User user = new User();
        user.setUsername(userDTO.username());
        user.setPassword(passwordEncoder.encode(userDTO.password()));

        repo.save(user);
    }


    public String loginUser(UserDTO userDTO) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.username(), userDTO.password())
            );

            return jwtService.generateToken(userDTO.username());
        }
        catch (Exception e) {
            throw new ApiRequestException("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }
    }

}
