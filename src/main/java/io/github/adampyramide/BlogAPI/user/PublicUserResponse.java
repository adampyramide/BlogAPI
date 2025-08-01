package io.github.adampyramide.BlogAPI.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicUserResponse {

    private Long id;
    private String username;
    private String avatarUrl;

}