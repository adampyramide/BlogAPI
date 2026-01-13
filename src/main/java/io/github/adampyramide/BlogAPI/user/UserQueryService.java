package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.error.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service for retrieving {@link User} entities.
 * <p>
 * Methods throw {@link ApiException} if the requested user does not exist.
 */
@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository repo;

    /**
     * Returns a {@link User} by its ID.
     *
     * @param id the user ID
     * @return the {@link User} with the given ID
     * @throws ApiException if no user exists with the given ID
     */
    public User getByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "USER_NOT_FOUND",
                        "User with ID " + id + " not found."
                ));
    }

}
