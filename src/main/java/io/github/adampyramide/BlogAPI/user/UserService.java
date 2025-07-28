package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.exception.CustomException;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    private final SecurityUtils securityUtils;

    public UserService(UserRepository repo, UserMapper mapper, SecurityUtils securityUtils) {
        this.repo = repo;
        this.mapper = mapper;
        this.securityUtils = securityUtils;
    }

    // ====================
    // Public methods
    // ====================

    public void deleteUser() {
        User user = securityUtils.getAuthenticatedUser();
        repo.delete(user);
    }

    public void updateUser(UpdateUserRequest userRequest) {
        User user = securityUtils.getAuthenticatedUser();

        mapper.updateEntity(userRequest, user);
        repo.save(user);
    }

    public PublicUserResponse getUserById(Long id) {
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