package io.github.adampyramide.BlogAPI.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(

        @NotBlank(message = "Username must not be blank")
        @Size(max = 20, message = "Username must be less than 20 characters")
        String username,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 6, max = 128, message = "Password must be between 6 and 128 characters")
        String password,

        @Email(message = "Invalid email format")
        String email

) {}
