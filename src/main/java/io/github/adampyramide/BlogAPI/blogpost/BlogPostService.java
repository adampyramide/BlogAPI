package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.exception.CustomException;
import io.github.adampyramide.BlogAPI.reaction.ReactionService;
import io.github.adampyramide.BlogAPI.reaction.ReactionType;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.User;
import io.github.adampyramide.BlogAPI.user.UserService;
import io.github.adampyramide.BlogAPI.util.OwnershipValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BlogPostService {

    private final BlogPostRepository repo;
    private final BlogPostFetcher validator;
    private final BlogPostMapper mapper;

    private final UserService userService;
    private final ReactionService reactionService;

    private final SecurityUtils securityUtils;

    public BlogPostService(BlogPostRepository repo, BlogPostFetcher validator, BlogPostMapper mapper, UserService userService, ReactionService reactionService, SecurityUtils securityUtils) {
        this.repo = repo;
        this.validator = validator;
        this.mapper = mapper;
        this.userService = userService;
        this.reactionService = reactionService;
        this.securityUtils = securityUtils;
    }

    // ====================
    // Public methods
    // ====================

    public Page<BlogPostResponseDTO> getBlogPosts(Pageable pageable) {
        return mapBlogPosts(repo.findAll(pageable));
    }

    public BlogPostResponseDTO getBlogPostById(Long id) {
        BlogPostResponseDTO blogPostDTO = mapper.toResponseDTO(validator.getByIdOrThrow(id));

        blogPostDTO.setUserReaction(reactionService.getUserReactionTypeForPost(
                securityUtils.getAuthenticatedUser().getId(),
                id
        ));

        Map<ReactionType, Long> reactionCounts = reactionService.getReactionCountsByPostId(id);
        blogPostDTO.setLikeCount(reactionCounts.getOrDefault(ReactionType.LIKE, 0L));
        blogPostDTO.setDislikeCount(reactionCounts.getOrDefault(ReactionType.DISLIKE, 0L));

        return blogPostDTO;
    }

    public void createBlogPost(BlogPostRequestDTO blogPostDTO) {
        BlogPost blogPost = mapper.toEntity(blogPostDTO);
        blogPost.setAuthor(securityUtils.getAuthenticatedUser());

        repo.save(blogPost);
    }

    public void editBlogPostById(Long id, BlogPostRequestDTO blogPostDTO) {
        BlogPost blogPost = validator.getByIdOrThrow(id);

        OwnershipValidator.authorizeAuthor(
                blogPost.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                "blogpost"
        );

        mapper.updateEntityWithDto(blogPostDTO, blogPost);
        repo.save(blogPost);
    }

    public void deleteBlogPostById(Long id) {
        BlogPost blogPost = validator.getByIdOrThrow(id);

        OwnershipValidator.authorizeAuthor(
                blogPost.getAuthor(),
                securityUtils.getAuthenticatedUser(),
                "blogpost"
        );

        repo.deleteById(id);
    }

    public void bulkDeleteBlogPostsByIds(List<Long> ids) {
        List<BlogPost> blogPosts = repo.findAllById(ids);

        if (blogPosts.isEmpty())
            throw new CustomException("Zero posts found", HttpStatus.NOT_FOUND);

        List<Long> foundPostIds = blogPosts.stream()
                .map(BlogPost::getId)
                .toList();
        List<Long> missingPostIds = ids.stream()
                .filter(id -> !foundPostIds.contains(id))
                .toList();

        User authenticatedUser = securityUtils.getAuthenticatedUser();
        for (BlogPost blogPost : blogPosts) {
            OwnershipValidator.authorizeAuthor(
                    blogPost.getAuthor(),
                    authenticatedUser,
                    "blogpost"
            );
        }

        repo.deleteAll(blogPosts);

        if (!missingPostIds.isEmpty())
            throw new CustomException("Some blogposts were not found: " + missingPostIds, HttpStatus.NOT_FOUND);

    }

    public Page<BlogPostResponseDTO> getBlogPostsByUserId(Long userId, Pageable pageable) {
        userService.getUserOrThrow(userId);

        return mapBlogPosts(repo.findAllByAuthor_Id(userId, pageable));
    }

    // ====================
    // Private methods
    // ====================

    private Page<BlogPostResponseDTO> mapBlogPosts(Page<BlogPost> page) {
        List<Long> postIds = page.getContent().stream()
                .map(BlogPost::getId)
                .toList();

        Long userId = securityUtils.getAuthenticatedUser().getId();
        Map<Long, ReactionType> userReactions = reactionService.getUserReactionTypesForPosts(userId, postIds);
        Map<Long, Map<ReactionType, Long>> reactionsCounts = reactionService.getReactionCountsForPostIds(postIds);

        return page.map(post -> {
            BlogPostResponseDTO blogPostDTO = mapper.toResponseDTO(post);
            Long postId = post.getId();

            blogPostDTO.setUserReaction(userReactions.get(postId));

            Map<ReactionType, Long> counts = reactionsCounts.getOrDefault(postId, Map.of());
            blogPostDTO.setLikeCount(counts.getOrDefault(ReactionType.LIKE, 0L));
            blogPostDTO.setDislikeCount(counts.getOrDefault(ReactionType.DISLIKE, 0L));

            return blogPostDTO;
        });
    }

}
