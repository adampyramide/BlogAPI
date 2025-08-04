package io.github.adampyramide.BlogAPI.user.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record UpdateUserRequest(

        String username,
        MultipartFile avatarImage,
        String description,

        Boolean removeAvatar

) {}
