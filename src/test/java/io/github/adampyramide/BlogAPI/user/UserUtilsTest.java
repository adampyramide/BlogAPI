package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.error.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserUtilsTest {

    private final String RESOURCE_NAME = "aResourceName";

    // ====================
    // Tests for validateOwnership
    // ====================

    @Test
    void validateOwnership_DoesNothing_WhenUserIsOwner() {
        // Arrange
        User user = User.builder()
                .id(1L).build();
        User author = User.builder()
                .id(1L).build();

        // Act & Assert
        assertDoesNotThrow(
                () -> UserUtils.validateOwnership(author, user, RESOURCE_NAME)
        );
    }

    @Test
    void validateOwnership_ThrowsException_WhenUserIsNotOwner() {
        // Arrange
        User user = User.builder()
                .id(99L).build();
        User author = User.builder()
                .id(1L).build();

        // Act & Assert
        ApiException exception = assertThrows(
                ApiException.class,
                () -> UserUtils.validateOwnership(author, user, RESOURCE_NAME)
        );

        // confirms that proper api exception is returned
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
        assertEquals("NOT_RESOURCE_OWNER", exception.getErrorCode());
        assertEquals(
                "User ID 99 attempted to access " + RESOURCE_NAME + " owned by user ID 1.",
                exception.getMessage()
        );
    }

}
