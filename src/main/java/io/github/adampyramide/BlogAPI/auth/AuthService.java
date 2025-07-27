package io.github.adampyramide.BlogAPI.auth;

import io.github.adampyramide.BlogAPI.exception.CustomException;
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

    public AuthResponse registerUser(AuthRequest userDTO) {
        if (userRepo.existsByUsername(userDTO.username()))
            throw new CustomException("Username is already taken", HttpStatus.CONFLICT);

        User user = userMapper.authDTOToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.password()));

        userRepo.save(user);

        return getAuthResponse(userDTO);
    }


    public AuthResponse loginUser(AuthRequest userDTO) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.username(), userDTO.password())
            );

            return getAuthResponse(userDTO);
        }
        catch (Exception e) {
            throw new CustomException("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    // ====================
    // Private methods
    // ====================

    private AuthResponse getAuthResponse(AuthRequest userDTO) {
        return new AuthResponse(jwtService.generateToken(userDTO.username()));
    }

}
