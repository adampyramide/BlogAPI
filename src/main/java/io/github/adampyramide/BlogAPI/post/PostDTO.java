package io.github.adampyramide.BlogAPI.post;

import io.github.adampyramide.BlogAPI.user.User;

import java.time.LocalDateTime;

public record PostDTO (
        String title,
        String content,
        LocalDateTime createTime,
        User author
) {}