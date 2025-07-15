package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.exception.CustomException;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.User;
import io.github.adampyramide.BlogAPI.user.UserService;
import io.github.adampyramide.BlogAPI.util.OwnershipValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogPostService {

    private final BlogPostRepository repo;
    private final BlogPostMapper mapper;

    private final UserService userService;

    private final SecurityUtils securityUtils;

    public BlogPostService(BlogPostRepository repo, BlogPostMapper blogPostMapper, UserService userService, SecurityUtils securityUtils) {
        this.repo = repo;
        this.mapper = blogPostMapper;
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    // ====================
    // Public methods
    // ====================

    public List<BlogPostResponseDTO> getBlogPosts() {
        return repo.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public BlogPostResponseDTO getBlogPostById(Long id) {
        return mapper.toResponseDTO(getBlogPostOrThrow(id));
    }

    public void createBlogPost(BlogPostRequestDTO blogPostDTO) {
        BlogPost blogPost = mapper.toEntity(blogPostDTO);
        blogPost.setAuthor(securityUtils.getAuthenticatedUser());
        blogPost.setCreateTime(LocalDateTime.now());

        repo.save(blogPost);
    }

    public void editBlogPostById(Long id, BlogPostRequestDTO blogPostDTO) {
        BlogPost blogPost = getBlogPostOrThrow(id);

        OwnershipValidator.authorizeAuthor(
                blogPost.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                "blogpost"
        );

        mapper.updateEntityWithDto(blogPostDTO, blogPost);
        repo.save(blogPost);
    }

    public void deleteBlogPostById(Long id) {
        BlogPost blogPost = getBlogPostOrThrow(id);

        OwnershipValidator.authorizeAuthor(
                blogPost.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                "blogpost"
        );

        repo.deleteById(id);
    }

    public void bulkDeletePostsByIds(List<Long> ids) {
        List<BlogPost> blogPosts = repo.findAllById(ids);

        if (blogPosts.size() != ids.size())
            throw new CustomException("Zero posts found", HttpStatus.NOT_FOUND);

        User authenticatedUser = securityUtils.getAuthenticatedUser();
        for (BlogPost blogPost : blogPosts) {
            OwnershipValidator.authorizeAuthor(
                    blogPost.getAuthor(),
                    authenticatedUser,
                    "blogpost"
            );
        }

        repo.deleteAll(blogPosts);
    }

    public List<BlogPostResponseDTO> getBlogPostsByUserId(Long userId) {
        userService.getUserOrThrow(userId);

        return repo.findAllByAuthor_Id(userId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    // ====================
    // Internal methods
    // ====================

    public BlogPost getBlogPostEntityById(Long id) {
        return getBlogPostOrThrow(id);
    }

    // ====================
    // Private methods
    // ====================

    private BlogPost getBlogPostOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("Blogpost not found", HttpStatus.NOT_FOUND));
    }

}
