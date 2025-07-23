package io.github.adampyramide.BlogAPI.util;

import io.github.adampyramide.BlogAPI.exception.CustomException;
import io.github.adampyramide.BlogAPI.user.User;
import org.springframework.http.HttpStatus;

public class OwnershipValidator {

    public static void authorizeAuthor(User author, User currentUser, String resourceName) {
        if (!author.getId().equals(currentUser.getId())) {
            throw new CustomException(
                    "You are not authorized to modify this " + resourceName, HttpStatus.FORBIDDEN
            );
        }
    }

}
