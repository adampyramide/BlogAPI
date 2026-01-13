package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.error.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Methods for retrieving {@link BlogPost} entities.
 */
@Service
@RequiredArgsConstructor
public class BlogPostQueryService {

    private final BlogPostRepository repo;

    /**
     * Returns a {@link BlogPost} by its ID.
     *
     * @param id the blog post ID
     * @return the {@link BlogPost} with the given ID
     * @throws ApiException if no blog post with the given ID exists
     */
    public BlogPost getByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "BLOGPOST_NOT_FOUND",
                        "Blog post with ID %s not found.".formatted(id)
                ));
    }

}
