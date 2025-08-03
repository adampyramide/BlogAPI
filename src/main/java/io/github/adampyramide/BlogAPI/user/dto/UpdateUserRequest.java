package io.github.adampyramide.BlogAPI.user.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserRequest(

        String username,
        MultipartFile avatarImage,
        String description,

        Boolean removeAvatar

) {}
