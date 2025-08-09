package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.auth.AuthRequest;
import io.github.adampyramide.BlogAPI.filestorage.CloudinaryFileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    UserMapperImpl underTest;

    @Mock
    private CloudinaryFileStorageService fileStorageService;

    @Test
    void toEntity_FromAuthRequest_MapsFieldsCorrectly() {
        // Arrange
        AuthRequest authRequest = AuthRequest.builder()
                .username("aUsername")
                .password("password")
                .build();

        // Act & Assert
        User result = assertDoesNotThrow(
                () -> underTest.toEntity(authRequest)
        );

        assertThat();
    }

}
