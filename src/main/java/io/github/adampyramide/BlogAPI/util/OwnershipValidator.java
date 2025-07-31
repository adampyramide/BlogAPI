package io.github.adampyramide.BlogAPI.util;

import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.user.User;
import org.springframework.http.HttpStatus;

public class OwnershipValidator {

    /**
     * Validates whether the current user is the owner of a resource.
     *
     * @param author        the user who owns the resource
     * @param user   the user performing the action
     * @param resourceName  a readable name for the resource (e.g., "comment", "post")
     * @throws ApiException if the current user is not the author
     */
    public static void authorizeAuthor(User author, User user, String resourceName) {
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
