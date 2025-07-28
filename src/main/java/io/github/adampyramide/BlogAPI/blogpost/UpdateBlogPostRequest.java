package io.github.adampyramide.BlogAPI.blogpost;

import jakarta.validation.constraints.Size;

public record UpdateBlogPostRequest(

        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title,

        @Size(max = 16000, message = "Body cannot exceed 16000 characters")
        String body

) {}