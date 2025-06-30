package io.github.adampyramide.BlogAPI.blogpost;

public record BlogPostRequestDTO(
        String title,
        String body
) {}