package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.filestorage.CloudinaryFileStorageService;
import io.github.adampyramide.BlogAPI.filestorage.FileUploadResult;
import io.github.adampyramide.BlogAPI.filestorage.FileValidationRule;
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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;
    private final UserQueryService queryService;

    private final SecurityUtils securityUtils;
    private final CloudinaryFileStorageService fileStorageService;

    private final FileValidationRule fileValidationRule = MimeTypeRules.IMAGE_ONLY;

    // ====================
    // Public methods
    // ====================

    public void deleteUser() {
        User user = securityUtils.getAuthenticatedUser();
        repo.delete(user);
    }

    public UserProfileResponse updateUser(UpdateUserRequest userRequest) {
        User user = securityUtils.getAuthenticatedUser();
        mapper.updateEntity(userRequest, user);

        validateDateOfBirth(userRequest.dateOfBirth());

        repo.save(user);

        return mapper.toUserProfileResponse(user);
    }

    public UserPreviewResponse updateUserAvatar(MultipartFile avatarImage) {
        User user = securityUtils.getAuthenticatedUser();
        FileUploadResult fileUploadResult = fileStorageService.save(
                avatarImage,
                fileValidationRule,
                "profile-pictures",
                user.getId().toString()
        );
        user.setAvatarId(fileUploadResult.id());

        repo.save(user);

        return mapper.toUserPreviewResponse(user);
    }

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

    public UserProfileResponse getUserById(Long id) {
        return mapper.toUserProfileResponse(queryService.getByIdOrThrow(id));
    }

    // ====================
    // Private methods
    // ====================

    private void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate minDate = today.minusYears(150);

        if (dateOfBirth.isBefore(minDate)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_DATE_OF_BIRTH",
                    "Date of birth is too old. Requested date of birth: " + dateOfBirth
            );
        }
    }

}