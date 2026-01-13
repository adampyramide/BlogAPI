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

/**
 * Service responsible for handling blog post use cases.
 *
 * <p>
 * Coordinates persistence, authorization, and mapping between entities and DTOs.
 * This service enforces ownership checks for modifying operations and delegates
 * entity retrieval and DTO assembly to dedicated collaborators.
 */
@Service
@RequiredArgsConstructor
public class BlogPostService {

    private static final String RESOURCE_NAME = BlogPost.class.getSimpleName();

    private final BlogPostRepository repo;
    private final BlogPostAssembler assembler;
    private final BlogPostQueryService queryService;
    private final BlogPostMapper mapper;
    private final UserQueryService userQueryService;
    private final SecurityUtils securityUtils;

    /**
     * Retrieves a paginated list of all blog posts.
     *
     * @param pageable pagination and sorting information
     * @return a page of {@link BlogPostResponse} objects
     */
    public Page<BlogPostResponse> getBlogPosts(Pageable pageable) {
        return assembler.mapAndEnrichToBlogPostResponses(repo.findAll(pageable));
    }

    /**
     * Retrieves a blog post by its ID.
     *
     * @param id the ID of the blog post
     * @return the {@link BlogPostResponse}
     * @throws ApiException if the blog post does not exist
     */
    public BlogPostResponse getBlogPostById(Long id) {
        return assembler.mapAndEnrichToBlogPostResponse(queryService.getByIdOrThrow(id));
    }

    /**
     * Creates a new blog post authored by the authenticated user.
     *
     * @param blogPostRequest the request containing blog post data
     * @return the created {@link BlogPostResponse}
     */
    public BlogPostResponse createBlogPost(CreateBlogPostRequest blogPostRequest) {
        BlogPost blogPost = mapper.toEntity(blogPostRequest);
        blogPost.setAuthor(securityUtils.getAuthenticatedUser());

        repo.save(blogPost);

        return assembler.mapAndEnrichToBlogPostResponse(blogPost);
    }

    /**
     * Updates an existing blog post.
     *
     * <p>
     * The authenticated user must be the owner of the blog post.
     *
     * @param id the ID of the blog post
     * @param blogPostRequest the update request
     * @return the updated {@link BlogPostResponse}
     * @throws ApiException if the blog post does not exist or the user is not the owner
     */
    public BlogPostResponse updateBlogPostById(Long id, UpdateBlogPostRequest blogPostRequest) {
        BlogPost blogPost = queryService.getByIdOrThrow(id);

        UserUtils.validateOwnership(
                blogPost.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                RESOURCE_NAME
        );

        mapper.updateEntity(blogPostRequest, blogPost);
        repo.save(blogPost);

        return assembler.mapAndEnrichToBlogPostResponse(blogPost);
    }

    /**
     * Deletes a blog post by its ID.
     *
     * <p>
     * The authenticated user must be the owner of the blog post.
     *
     * @param id the ID of the blog post
     * @throws ApiException if the blog post does not exist or the user is not the owner
     */
    public void deleteBlogPostById(Long id) {

        BlogPost blogPost = queryService.getByIdOrThrow(id);

        UserUtils.validateOwnership(
                blogPost.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                RESOURCE_NAME
        );

        repo.deleteById(id);
    }

    /**
     * Deletes multiple blog posts by their IDs.
     *
     * <p>
     * All requested blog posts must exist and must be owned by the authenticated user.
     *
     * @param ids the IDs of the blog posts to delete
     * @throws ApiException if any blog post does not exist or is not owned by the user
     */
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

    /**
     * Retrieves a paginated list of blog posts authored by a specific user.
     *
     * @param userId the ID of the author
     * @param pageable pagination and sorting information
     * @return a page of {@link BlogPostResponse} objects
     * @throws ApiException if the user does not exist
     */
    public Page<BlogPostResponse> getBlogPostsByUserId(Long userId, Pageable pageable) {
        userQueryService.getByIdOrThrow(userId);
        return assembler.mapAndEnrichToBlogPostResponses(repo.findAllByAuthorId(userId, pageable));
    }

}
