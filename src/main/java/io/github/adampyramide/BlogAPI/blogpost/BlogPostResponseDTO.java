package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.user.PublicUserDTO;

import java.time.LocalDateTime;

public record BlogPostResponseDTO(

        int id,
        String title,
        String body,
        LocalDateTime createTime,
        PublicUserDTO author

) {}