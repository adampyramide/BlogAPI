package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.filestorage.CloudinaryFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAssembler {

    private final UserMapper mapper;
    private final UserQueryService queryService;

    private final CloudinaryFileStorageService fileStorageService;

    public PublicUserResponse getUserResponseById(Long id) {
        User user = queryService.getUserOrThrow(id);
        PublicUserResponse userResponse = mapper.toPublicDTO(user);
        userResponse.setProfilePictureUrl(fileStorageService.getUrl(user.getProfilePictureId()));

        return userResponse;
    }

}
