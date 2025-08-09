package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.error.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserUtilsTest {

    private final String resourceName = "aResource";

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
                () -> UserUtils.validateOwnership(author, user, resourceName)
        );
    }

    @Test
    void validateOwnership_ThrowsException_WhenUserIsNotOwner() {
        // Arrange
        User user = User.builder()
                .id(1L).build();
        User author = User.builder()
                .id(2L).build();

        // Act & Assert
        ApiException exception = assertThrows(
                ApiException.class,
                () -> UserUtils.validateOwnership(author, user, resourceName)
        );

        // confirms that proper api exception is returned
        assertEquals("NOT_RESOURCE_OWNER", exception.getErrorCode());
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
