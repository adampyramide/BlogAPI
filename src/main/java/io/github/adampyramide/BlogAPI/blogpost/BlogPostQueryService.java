package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.error.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogPostQueryService {

    private final BlogPostRepository repo;

    public BlogPost getByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "BLOGPOST_NOT_FOUND",
                        "Blog post with ID %s not found".formatted(id)
                ));
    }

}
