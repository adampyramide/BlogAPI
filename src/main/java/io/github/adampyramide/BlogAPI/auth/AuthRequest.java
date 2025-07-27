package io.github.adampyramide.BlogAPI.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(

        @NotBlank(message = "Username must not be blank")
        String username,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 6, max = 128, message = "Password must be between 6 and 128 characters")
        String password

) {}
