package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.user.PublicUserDTO;

import java.time.LocalDateTime;

public record BlogPostResponseDTO(

        Long id,
        PublicUserDTO author,
        String title,
        String body,
        LocalDateTime createTime,
        int likeCount,
        int dislikeCount

) {}