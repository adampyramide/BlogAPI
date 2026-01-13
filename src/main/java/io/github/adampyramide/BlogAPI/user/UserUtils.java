package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.error.ApiException;
import org.springframework.http.HttpStatus;

/**
 * Methods commonly used by other classes
 */
public class UserUtils {

    /**
     * Validates whether a {@link User} is the owner of a resource.
     *
     * @param author the {@link User} who owns the resource
     * @param user the {@link User} attempting to access the resource
     * @param resourceName a readable name for the resource (e.g. "comment", "blogpost")
     * @throws ApiException if the user is not the resource owner
     */
    public static void validateOwnership(User author, User user, String resourceName) {
        if (!author.getId().equals(user.getId())) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "NOT_RESOURCE_OWNER",
                    "User ID %d attempted to access %s owned by user ID %d."
                            .formatted(user.getId(), resourceName, author.getId())
            );
        }
    }

}
