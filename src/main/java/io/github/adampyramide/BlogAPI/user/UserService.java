package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.filestorage.CloudinaryFileStorageService;
import io.github.adampyramide.BlogAPI.filestorage.FileUploadResult;
import io.github.adampyramide.BlogAPI.filestorage.MimeTypeRules;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.dto.UpdateUserRequest;
import io.github.adampyramide.BlogAPI.user.dto.UserPreviewResponse;
import io.github.adampyramide.BlogAPI.user.dto.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * Service for managing {@link User} entities.
 * <p>
 * Provides methods to update, delete, and retrieve users and their avatars.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    static final int MAX_DATE_OF_BIRTH_YEARS = 150;

    private final UserRepository repo;
    private final UserMapper mapper;
    private final UserQueryService queryService;
    private final SecurityUtils securityUtils;
    private final CloudinaryFileStorageService fileStorageService;

    /**
     * Deletes the authenticated {@link User}.
     *
     * @throws ApiException if no authenticated user is found
     */
    public void deleteUser() {
        User user = securityUtils.getAuthenticatedUser();
        repo.delete(user);
    }

    /**
     * Updates the authenticated {@link User} with the provided data.
     *
     * @param userRequest the data to update
     * @return the updated {@link UserProfileResponse}
     * @throws ApiException if validation fails or no authenticated user is found
     */
    public UserProfileResponse updateUser(UpdateUserRequest userRequest) {
        User user = securityUtils.getAuthenticatedUser();
        mapper.updateEntity(userRequest, user);

        validateDateOfBirth(userRequest.dateOfBirth());

        repo.save(user);

        return mapper.toUserProfileResponse(user);
    }

    /**
     * Updates the authenticated {@link User}'s avatar.
     *
     * @param avatarImage the new avatar image
     * @return the updated {@link UserPreviewResponse}
     * @throws ApiException if file validation fails or no authenticated user is found
     */
    public UserPreviewResponse updateUserAvatar(MultipartFile avatarImage) {
        User user = securityUtils.getAuthenticatedUser();
        FileUploadResult fileUploadResult = fileStorageService.save(
                avatarImage,
                MimeTypeRules.IMAGE_ONLY,
                "profile-pictures",
                user.getId().toString()
        );
        user.setAvatarId(fileUploadResult.id());

        repo.save(user);

        return mapper.toUserPreviewResponse(user);
    }

    /**
     * Deletes the currently authenticated {@link User}'s avatar.
     *
     * @throws ApiException if no authenticated user is found
     */
    public void deleteUserAvatar() {
        User user = securityUtils.getAuthenticatedUser();
        String avatarId = user.getAvatarId();
        if (avatarId == null) {
            return;
        }
        fileStorageService.delete(avatarId);
        user.setAvatarId(null);

        repo.save(user);
    }

    /**
     * Retrieves a {@link UserProfileResponse} for a given user ID.
     *
     * @param id the user ID
     * @return the {@link UserProfileResponse} for the given ID
     * @throws ApiException if no user exists with the given ID
     */
    public UserProfileResponse getUserById(Long id) {
        return mapper.toUserProfileResponse(queryService.getByIdOrThrow(id));
    }

    /**
     * Validates the user's date of birth against the maximum allowed age.
     */
    private void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate minDate = today.minusYears(MAX_DATE_OF_BIRTH_YEARS);

        if (dateOfBirth.isBefore(minDate)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_DATE_OF_BIRTH",
                    "Date of birth is too old. Requested date of birth: " + dateOfBirth
            );
        }
    }

}