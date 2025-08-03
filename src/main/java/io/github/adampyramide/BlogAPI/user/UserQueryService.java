package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.error.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository repo;

    public User getByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "USER_NOT_FOUND",
                        "User with ID " + id + " not found."
                ));
    }

}
