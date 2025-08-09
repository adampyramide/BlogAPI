package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.filestorage.CloudinaryFileStorageService;
import io.github.adampyramide.BlogAPI.filestorage.FileUploadResult;
import io.github.adampyramide.BlogAPI.filestorage.FileValidationRule;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.dto.UpdateUserRequest;
import io.github.adampyramide.BlogAPI.user.dto.UserProfileResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService underTest;

    @Mock
    private UserRepository repo;
    @Mock
    private UserMapper mapper;
    @Mock
    private UserQueryService queryService;
    @Mock
    private SecurityUtils securityUtils;
    @Mock
    private CloudinaryFileStorageService fileStorageService;

    // ====================
    // Test Helper Methods
    // ====================

    private User createMockUser() {
        return User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .description("description")
                .avatarId(null)
                .build();
    }

    private void prepareGetAuthenticatedUser(User user) {
        when(securityUtils.getAuthenticatedUser()).thenReturn(user);
    }

    private void prepareUpdateUserTests(User user, UpdateUserRequest userRequest) {
        prepareGetAuthenticatedUser(user);

        doAnswer(invocation -> {
            user.setUsername(userRequest.username());
            user.setDisplayName(userRequest.displayName());
            user.setDescription(userRequest.description());
            user.setDateOfBirth(userRequest.dateOfBirth());
            user.setGender(userRequest.gender());
            return null;
        }).when(mapper).updateEntity(userRequest, user);
    }

    // ====================
    // Tests for GetUserById
    // ====================

    @Test
    void getUserById_ReturnsMappedUserProfile_WhenUserExists() {
        // Arrange
        User user = createMockUser();
        UserProfileResponse response = UserProfileResponse.builder()
                .build();

        when(queryService.getByIdOrThrow(user.getId())).thenReturn(user);
        when(mapper.toUserProfileResponse(user)).thenReturn(response);

        // Act
        UserProfileResponse result = assertDoesNotThrow(
                () -> underTest.getUserById(user.getId())
        );

        // Assert
        assertThat(result).isEqualTo(response);
    }

    // ====================
    // Tests for DeleteUser
    // ====================

    @Test
    void deleteUser_DeletesUser_WhenRequested() {
        // Arrange
        User user = createMockUser();
        when(securityUtils.getAuthenticatedUser()).thenReturn(user);

        // Act & Assert
        assertDoesNotThrow(
                () -> underTest.deleteUser()
        );
        verify(repo).delete(user); // user deleted from DB
    }

    // ====================
    // Tests for UpdateUser
    // ====================

    @ParameterizedTest
    @CsvSource({
            "newUsername,newDisplayName,newDescription,2000-01-01,RATHER_NOT_SAY",
            "newUsername,,,,",
            ",newDisplayName,,,",
            ",,newDescription,,",
            ",,,2000-01-01,",
            ",,,,RATHER_NOT_SAY",
            ",,,,,"
    })
    void updateUser_UpdatesSpecifiedFieldsInUser_WhenFieldsProvided(
            String username,
            String displayName,
            String description,
            String dateOfBirthStr,
            String genderStr
    ) {
        // Arrange
        User user = createMockUser();

        LocalDate dateOfBirth = (dateOfBirthStr == null || dateOfBirthStr.isBlank())
                ? null : LocalDate.parse(dateOfBirthStr);

        Gender gender = (genderStr == null || genderStr.isBlank())
                ? null : Gender.valueOf(genderStr);

        UpdateUserRequest userRequest = UpdateUserRequest.builder()
                .username(username)
                .displayName(displayName)
                .description(description)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .build();

        prepareUpdateUserTests(user, userRequest);

        UserProfileResponse response = UserProfileResponse.builder()
                .username(username)
                .displayName(displayName)
                .description(description)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .build();
        when(mapper.toUserProfileResponse(any(User.class))).thenReturn(response);

        // Act & Assert
        UserProfileResponse result = assertDoesNotThrow(
                () -> underTest.updateUser(userRequest)
        );

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repo).save(userCaptor.capture()); // user saved to repo

        User savedUser = userCaptor.getValue();

        // confirms that: savedUser values equal to userRequest, result values equal to parameterized values
        if (username != null) {
            assertThat(savedUser.getUsername()).isEqualTo(userRequest.username());
            assertThat(result.getUsername()).isEqualTo(username);
        }
        if (displayName != null) {
            assertThat(savedUser.getDisplayName()).isEqualTo(userRequest.displayName());
            assertThat(result.getDisplayName()).isEqualTo(displayName);
        }
        if (description != null) {
            assertThat(savedUser.getDescription()).isEqualTo(userRequest.description());
            assertThat(result.getDescription()).isEqualTo(description);
        }
        if (dateOfBirth != null) {
            assertThat(savedUser.getDateOfBirth()).isEqualTo(userRequest.dateOfBirth());
            assertThat(result.getDateOfBirth()).isEqualTo(dateOfBirth);
        }
        if (gender != null) {
            assertThat(savedUser.getGender()).isEqualTo(userRequest.gender());
            assertThat(result.getGender()).isEqualTo(gender);
        }
    }

    @Test
    void updateUser_UpdatesDateOfBirth_WhenDateIsAlmostTooOld() {
        // Arrange
        User user = createMockUser();

        LocalDate dateOfBirth = LocalDate.now().minusYears(1);

        UpdateUserRequest userRequest = UpdateUserRequest.builder()
                .dateOfBirth(dateOfBirth)
                .build();

        prepareUpdateUserTests(user, userRequest);

        UserProfileResponse response = UserProfileResponse.builder()
                .dateOfBirth(dateOfBirth)
                .build();
        when(mapper.toUserProfileResponse(any(User.class))).thenReturn(response);

        // Act & Assert
        UserProfileResponse result = assertDoesNotThrow(
                () -> underTest.updateUser(userRequest)
        );
        assertThat(result).isEqualTo(response);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(repo).save(userCaptor.capture()); // user saved to DB

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getDateOfBirth()).isEqualTo(dateOfBirth); // savedUser dateOfBirth same as input
        assertThat(result.getDateOfBirth()).isEqualTo(dateOfBirth); // returned result dateOfBirth same as input
    }

    @Test
    void updateUser_ThrowExceptions_WhenDateOfBirthIsTooOld() {
        // Arrange
        User user = createMockUser();

        UpdateUserRequest userRequest = UpdateUserRequest.builder()
                .dateOfBirth(LocalDate.now().minusDays(1).minusYears(UserService.MAX_DATE_OF_BIRTH_YEARS))
                .build();

        prepareUpdateUserTests(user, userRequest);

        // Act & Assert
        ApiException ex = assertThrows(
                ApiException.class,
                () -> underTest.updateUser(userRequest)
        );

        // confirms that proper api exception is returned
        assertEquals("INVALID_DATE_OF_BIRTH", ex.getErrorCode());
        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertTrue(ex.getMessage().contains("Date of birth is too old"));
    }

    // ====================
    // Tests for UpdateUserAvatar
    // ====================

    @Test
    void updateUserAvatar_StoresAvatarId_WhenAvatarImageProvided() {
        // Arrange
        User user = createMockUser();

        MultipartFile file = new MockMultipartFile(
                "avatar",
                "avatar.png",
                "image/png",
                "fake image content".getBytes()
        );

        prepareGetAuthenticatedUser(user);

        when(fileStorageService.save(
                eq(file),
                any(FileValidationRule.class),
                eq("profile-pictures"),
                eq(user.getId().toString())
        )).thenReturn(new FileUploadResult("fake-avatar-id", "https://fake-url.com"));

        // Act & Assert
        assertDoesNotThrow(
                () -> underTest.updateUserAvatar(file)
        );

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repo).save(userCaptor.capture()); // saved in DB
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getAvatarId()).isEqualTo("fake-avatar-id"); // avatarId set in entity
    }

    @Test
    void updateUserAvatar_ThrowsExceptionAndDoesNotSaveUser_WhenFileUploadFails() {
        // Arrange
        User user = createMockUser();

        MultipartFile file = new MockMultipartFile(
                "notAnImage",
                "notanimage.txt",
                "image/txt",
                "fake text file content".getBytes()
        );

        prepareGetAuthenticatedUser(user);

        when(fileStorageService.save(any(), any(), any(), any()))
                .thenThrow(new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "TEST",
                        "Test"
                ));

        // Act
        assertThrows(
                ApiException.class,
                () -> underTest.updateUserAvatar(file)
        );

        // Assert
        verify(fileStorageService).save(any(), any(), any(), any());
        verify(repo, never()).save(any()); // not saved in DB
    }

    // ====================
    // Tests for deleteUserAvatar
    // ====================

    @Test
    void deleteUserAvatar_DeletesAvatarId_WhenRequested() {
        // Arrange
        User user = createMockUser();
        user.setAvatarId("fake-avatar-id");

        prepareGetAuthenticatedUser(user);
        doNothing().when(fileStorageService).delete("fake-avatar-id");

        // Act & Assert
        assertDoesNotThrow(
                () -> underTest.deleteUserAvatar()
        );

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repo).save(userCaptor.capture()); //saved in DB
        User savedUser = userCaptor.getValue();
        verify(fileStorageService).delete("fake-avatar-id"); // deleted from cloud
        assertThat(savedUser.getAvatarId()).isNull(); // avatarId removed from entity
    }

    @Test
    void deleteUserAvatar_DoesNothing_WhenUserHasNoAvatarIdSaved() {
        // Arrange
        User user = createMockUser();
        user.setAvatarId(null);

        prepareGetAuthenticatedUser(user);

        // Act & Assert
        assertDoesNotThrow(
                () -> underTest.deleteUserAvatar()
        );

        verify(repo, never()).save(any()); // not saved in DB
        verify(fileStorageService, never()).delete(any()); // not delete attempt to cloud
    }

}