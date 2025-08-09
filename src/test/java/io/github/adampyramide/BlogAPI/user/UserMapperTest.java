package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.auth.AuthRequest;
import io.github.adampyramide.BlogAPI.filestorage.CloudinaryFileStorageService;
import io.github.adampyramide.BlogAPI.user.dto.UpdateUserRequest;
import io.github.adampyramide.BlogAPI.user.dto.UserPreviewResponse;
import io.github.adampyramide.BlogAPI.user.dto.UserProfileResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    UserMapperImpl underTest;

    @Mock
    private CloudinaryFileStorageService fileStorageService;

    // ====================
    // Test Helper Methods
    // ====================

    private User createMockUser() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(18);

        return User.builder()
                .id(1L)
                .username("aUsername")
                .password("aPassword")
                .email("anemail@google.com")
                .avatarId("anAvatarId")
                .description("aDescription")
                .dateOfBirth(dateOfBirth)
                .gender(Gender.UNSPECIFIED)
                .build();
    }

    // ====================
    // Tests
    // ====================

    @Test
    void toEntity_givenAuthRequest_mapsFieldsCorrectly() {
        // Arrange
        AuthRequest authRequest = AuthRequest.builder()
                .username("aUsername")
                .password("aPassword")
                .build();

        // Act & Assert
        User result = assertDoesNotThrow(
                () -> underTest.toEntity(authRequest)
        );

        assertThat(result.getId()).isNull();
        assertThat(result.getUsername()).isEqualTo("aUsername");
        assertThat(result.getPassword()).isEqualTo("aPassword");
    }

    @Test
    void toUserPreviewResponse_givenEntity_returnsCorrectResponse() {
        // Arrange
        User user = createMockUser();
        String urlTest = "https://cdn.example.com/anAvatarId.jpg";
        when(fileStorageService.getUrl(eq("anAvatarId"), any()))
                .thenReturn(urlTest);

        UserPreviewResponse expected = UserPreviewResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .avatarUrl(urlTest)
                .build();

        // Act & Assert
        UserPreviewResponse result = assertDoesNotThrow(
                () -> underTest.toUserPreviewResponse(user)
        );

        assertThat(result)
                .usingRecursiveAssertion()
                .isEqualTo(expected);
    }

    @Test
    void toUserProfileResponse_givenEntity_returnsCorrectResponse() {
        // Arrange
        User user = createMockUser();
        String fakeUrl = "https://cdn.example.com/anAvatarId.jpg";
        when(fileStorageService.getUrl(eq("anAvatarId"), any()))
                .thenReturn(fakeUrl);

        UserProfileResponse expected = UserProfileResponse.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .username(user.getUsername())
                .avatarUrl(fakeUrl)
                .displayName(user.getDisplayName())
                .description(user.getDescription())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();

        // Act & Assert
        UserProfileResponse result = assertDoesNotThrow(
                () -> underTest.toUserProfileResponse(user)
        );

        assertThat(result)
                .usingRecursiveAssertion()
                .isEqualTo(expected);
    }

    @Test
    void updateEntity_givenUpdateUserRequest_UpdatesEntityCorrectly() {
        // Arrange
        User user = createMockUser();

        LocalDate newDateOfBirth = LocalDate.now().minusYears(5);
        UpdateUserRequest updateRequest = UpdateUserRequest.builder()
                .username("newUsername")
                .displayName("newDisplayName")
                .description("newDescription")
                .dateOfBirth(newDateOfBirth)
                .gender(Gender.RATHER_NOT_SAY)
                .build();

        User expected = createMockUser();
        expected.setUsername(updateRequest.username());
        expected.setDisplayName(updateRequest.displayName());
        expected.setDescription(updateRequest.description());
        expected.setDateOfBirth(updateRequest.dateOfBirth());
        expected.setGender(updateRequest.gender());

        // Act
        assertDoesNotThrow(
                () -> underTest.updateEntity(updateRequest, user)
        );

        assertThat(user)
                .usingRecursiveAssertion()
                .isEqualTo(expected);
    }

}
