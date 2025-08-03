package io.github.adampyramide.BlogAPI.auth;

import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.security.JwtService;
import io.github.adampyramide.BlogAPI.user.User;
import io.github.adampyramide.BlogAPI.user.UserMapper;
import io.github.adampyramide.BlogAPI.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final UserMapper userMapper;

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepo, UserMapper userMapper, AuthenticationManager authManager, JwtService jwtService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // ====================
    // Public methods
    // ====================

    public AuthResponse registerUser(AuthRequest authRequest) {
        if (userRepo.existsByUsername(authRequest.username()))
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "USERNAME_TAKEN",
                    "Username '%s' is already taken.".formatted(authRequest.username())
            );

        User user = userMapper.toEntity(authRequest);
        user.setPassword(passwordEncoder.encode(authRequest.password()));

        userRepo.save(user);

        return getAuthResponse(authRequest);
    }


    public AuthResponse loginUser(AuthRequest authRequest) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
            );

            return getAuthResponse(authRequest);
        }
        catch (Exception e) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "INVALID_CREDENTIALS",
                    "Invalid username or password."
            );
        }
    }

    // ====================
    // Private methods
    // ====================

    private AuthResponse getAuthResponse(AuthRequest authRequest) {
        return new AuthResponse(jwtService.generateToken(authRequest.username()));
    }

}
