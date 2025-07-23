package io.github.adampyramide.BlogAPI.util;

import io.github.adampyramide.BlogAPI.exception.CustomException;
import io.github.adampyramide.BlogAPI.user.User;
import org.springframework.http.HttpStatus;

public class OwnershipValidator {

    /**
     * Validates whether the current user is the owner of a resource.
     *
     * @param author        the user who owns the resource
     * @param currentUser   the user performing the action
     * @param resourceName  a readable name for the resource (e.g., "comment", "post")
     * @throws CustomException if the current user is not the author
     */
    public static void authorizeAuthor(User author, User currentUser, String resourceName) {
        if (!author.getId().equals(currentUser.getId())) {
            throw new CustomException(
                    "You are not authorized to modify this " + resourceName, HttpStatus.FORBIDDEN
            );
        }
    }

}
