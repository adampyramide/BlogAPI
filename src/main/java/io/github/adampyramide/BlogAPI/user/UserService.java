package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.filestorage.CloudinaryFileStorageService;
import io.github.adampyramide.BlogAPI.filestorage.FileUploadResult;
import io.github.adampyramide.BlogAPI.filestorage.FileValidationRule;
import io.github.adampyramide.BlogAPI.filestorage.MimeTypeRules;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.dto.UserPreviewResponse;
import io.github.adampyramide.BlogAPI.user.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;
    private final UserAssembler assembler;

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

    public void updateUser(UpdateUserRequest userRequest) {
        User user = securityUtils.getAuthenticatedUser();
        mapper.updateEntity(userRequest, user);

        handleAvatarUpdate(user,
                userRequest.avatarImage(),
                userRequest.removeAvatar()
        );

        repo.save(user);
    }

    public UserPreviewResponse getUserById(Long id) {
        return assembler.getUserResponseById(id);
    }

    // ====================
    // Private methods
    // ====================

    /**
     * Handles avatar updates for a user.
     * <p>
     * Based on the provided flags, this will either remove the existing avatar,
     * upload a new one, or do nothing. Uploading a new avatar automatically
     * replaces any existing avatar image.
     * </p>
     *
     * @param user the user whose avatar is being updated
     * @param avatarImage the new avatar image to upload (nullable)
     * @param removeAvatar true if the current avatar should be removed
     * @throws ApiException if both upload and removal are requested in the same call
     */
    private void handleAvatarUpdate(User user, MultipartFile avatarImage, Boolean removeAvatar) {
        boolean wantsToRemove = Boolean.TRUE.equals(removeAvatar);
        boolean wantsToUpload = avatarImage != null && !avatarImage.isEmpty();

        if (wantsToRemove && wantsToUpload) {
            throw  new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "AVATAR_UPDATE_CONFLICT",
                    "Cannot request both avatar removal and upload in the same request. " +
                            "Note: uploading a new avatar automatically replaces the existing one."
            );
        }

        String oldAvatarId = user.getAvatarId();
        if (wantsToRemove && oldAvatarId != null) {
            fileStorageService.delete(oldAvatarId);
            user.setAvatarId(null);
        }

        if (wantsToUpload) {
            FileUploadResult fileUploadResult = fileStorageService.save(
                    avatarImage,
                    fileValidationRule,
                    "profile-pictures",
                    user.getId().toString()
            );

            user.setAvatarId(fileUploadResult.id());
        }
    }

}