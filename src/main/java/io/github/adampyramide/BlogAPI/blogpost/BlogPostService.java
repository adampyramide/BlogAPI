package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.blogpost.dto.BlogPostResponse;
import io.github.adampyramide.BlogAPI.blogpost.dto.CreateBlogPostRequest;
import io.github.adampyramide.BlogAPI.blogpost.dto.UpdateBlogPostRequest;
import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.User;
import io.github.adampyramide.BlogAPI.user.UserQueryService;
import io.github.adampyramide.BlogAPI.user.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogPostService {

    // Constants
    private static final String RESOURCE_NAME = BlogPost.class.getSimpleName();

    // Dependencies
    private final BlogPostRepository repo;
    private final BlogPostAssembler assembler;
    private final BlogPostQueryService queryService;
    private final BlogPostMapper mapper;

    private final UserQueryService userQueryService;
    private final SecurityUtils securityUtils;

    // ====================
    // Public methods
    // ====================

    public Page<BlogPostResponse> getBlogPosts(Pageable pageable) {
        return assembler.mapToBlogPostResponses(repo.findAll(pageable));
    }

    public BlogPostResponse getBlogPostById(Long id) {
        return assembler.mapToBlogPostResponse(queryService.getByIdOrThrow(id));
    }

    public BlogPostResponse createBlogPost(CreateBlogPostRequest blogPostRequest) {
        BlogPost blogPost = mapper.toEntity(blogPostRequest);
        blogPost.setAuthor(securityUtils.getAuthenticatedUser());

        repo.save(blogPost);

        return assembler.mapToBlogPostResponse(blogPost);
    }

    public BlogPostResponse updateBlogPostById(Long id, UpdateBlogPostRequest blogPostRequest) {
        BlogPost blogPost = queryService.getByIdOrThrow(id);

        UserUtils.validateOwnership(
                blogPost.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                RESOURCE_NAME
        );

        mapper.updateEntity(blogPostRequest, blogPost);
        repo.save(blogPost);

        return assembler.mapToBlogPostResponse(blogPost);
    }

    public void deleteBlogPostById(Long id) {

        BlogPost blogPost = queryService.getByIdOrThrow(id);

        UserUtils.validateOwnership(
                blogPost.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                RESOURCE_NAME
        );

        repo.deleteById(id);
    }

    public void bulkDeleteBlogPostsByIds(List<Long> ids) {
        List<BlogPost> blogPosts = repo.findAllById(ids);

        List<Long> foundPostIds = blogPosts.stream()
                .map(BlogPost::getId)
                .toList();
        List<Long> missingPostIds = ids.stream()
                .filter(id -> !foundPostIds.contains(id))
                .toList();

        if (!missingPostIds.isEmpty()) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "BLOGPOSTS_NOT_FOUND",
                    "Blogposts not found with IDs: " + missingPostIds
            );
        }

        User authenticatedUser = securityUtils.getAuthenticatedUser();
        for (BlogPost blogPost : blogPosts) {
            UserUtils.validateOwnership(
                    blogPost.getAuthor(),
                    authenticatedUser,
                    RESOURCE_NAME
            );
        }

        repo.deleteAll(blogPosts);
    }

    public Page<BlogPostResponse> getBlogPostsByUserId(Long userId, Pageable pageable) {
        userQueryService.getByIdOrThrow(userId);
        return assembler.mapToBlogPostResponses(repo.findAllByAuthorId(userId, pageable));
    }

}
