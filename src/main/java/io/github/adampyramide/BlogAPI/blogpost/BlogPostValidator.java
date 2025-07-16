package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BlogPostValidator {

    private final BlogPostRepository repo;

    public BlogPostValidator(BlogPostRepository repo) {
        this.repo = repo;
    }

    public BlogPost getByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("Blogpost not found", HttpStatus.NOT_FOUND));
    }

}
