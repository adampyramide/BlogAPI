package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.error.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserQueryServiceTest {

    @InjectMocks
    private UserQueryService underTest;

    @Mock
    private UserRepository repo;

    // ====================
    // Tests for getByIdOrThrow
    // ====================

    @Test
    void getByIdOrThrow_ReturnsUser_IfExists() {
        // Arrange
        User user = User.builder()
                .id(1L).build();
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = assertDoesNotThrow(
                () -> underTest.getByIdOrThrow(1L)
        );

        // Assert
        assertEquals(1L, result.getId());
    }

    @Test
    void getByIdOrThrow_ThrowsException_IfNotFound() {
        // Arrange
        Long id = 99L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        // Act
        ApiException exception = assertThrows(
                ApiException.class,
                () -> underTest.getByIdOrThrow(id)
        );

        // Assert
        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("USER_NOT_FOUND", exception.getErrorCode());
        assertEquals("User with ID 99 not found.", exception.getMessage());
    }

}
