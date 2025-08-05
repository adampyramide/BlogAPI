package io.github.adampyramide.BlogAPI.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.adampyramide.BlogAPI.user.Gender;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdateUserRequest(

        @Size(min = 1, max = 20, message = "Username must be between 1 and 20 characters")
        String username,

        @Size(min = 1, max = 20, message = "Display name must be between 1 and 20 characters")
        String displayName,

        @Size(max = 500, message = "Description must be less than 500 characters")
        String description,

        @Past(message = "Date of birt must be in the past")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateOfBirth,

        Gender gender

) {}
