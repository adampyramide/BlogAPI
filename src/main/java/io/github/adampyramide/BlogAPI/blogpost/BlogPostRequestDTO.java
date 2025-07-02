package io.github.adampyramide.BlogAPI.blogpost;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BlogPostRequestDTO(

        @NotBlank(message = "Title must not be blank")
        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title,

        @NotBlank(message = "Body must not be blank")
        @Size(max = 16000, message = "Body cannot exceed 16000 characters")
        String body

) {}