package io.github.adampyramide.BlogAPI.user.dto;

import io.github.adampyramide.BlogAPI.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

    private Long id;
    private LocalDateTime createdAt;
    private String username;
    private String avatarUrl;
    private String displayName;
    private String description;
    private LocalDate dateOfBirth;
    private Gender gender;

}