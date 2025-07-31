package io.github.adampyramide.BlogAPI.security;

import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.user.User;
import io.github.adampyramide.BlogAPI.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepo;

    public User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "AUTHENTICATED_USER_NOT_FOUND",
                        "Authenticated user not found"
                ));
    }

}
