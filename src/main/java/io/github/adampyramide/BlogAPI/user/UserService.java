package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    public UserService(UserRepository userRepo, UserMapper mapper) {
        this.repo = userRepo;
        this.mapper = mapper;
    }

    // ====================
    // Public methods
    // ====================

    public PublicUserDTO getUserById(Long id) {
        return mapper.toPublicDTO(getUserOrThrow(id));
    }

    // ====================
    // Internal methods
    // ====================

    public User getUserOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }

}