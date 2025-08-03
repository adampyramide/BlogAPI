package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.filestorage.CloudinaryFileStorageService;
import io.github.adampyramide.BlogAPI.filestorage.FileValidationRule;
import io.github.adampyramide.BlogAPI.filestorage.MimeTypeRules;
import io.github.adampyramide.BlogAPI.user.dto.UserPreviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAssembler {

    private final UserMapper mapper;
    private final UserQueryService queryService;

    private final CloudinaryFileStorageService fileStorageService;

    private final FileValidationRule fileValidationRule = MimeTypeRules.IMAGE_ONLY;

    // ====================
    // Public methods
    // ====================

    /**
     * Gets a user by ID from the database, turns it into a DTO. enriches it and returns it.
     *
     * @param id user ID
     * @throws ApiException if no user found with ID
     */
    public UserPreviewResponse getUserResponseById(Long id) {
        User user = queryService.getUserOrThrow(id);
        return enrichUserResponse(user, mapper.toPublicDTO(user));
    }

    /**
     * Enriches a PublicUserResponse
     *
     * @param user the user entity
     * @param userResponse the userResponse DTO
     */
    public UserPreviewResponse enrichUserResponse(User user, UserPreviewResponse userResponse) {
        userResponse.setAvatarUrl(fileStorageService.getUrl(user.getAvatarId(), fileValidationRule));
        return userResponse;
    }

    // ====================
    // Private methods
    // ====================



}
