package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.blogpost.dto.BlogPostResponse;
import io.github.adampyramide.BlogAPI.reaction.ReactionService;
import io.github.adampyramide.BlogAPI.reaction.ReactionType;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Assembler responsible for converting {@link BlogPost} entities into {@link BlogPostResponse} DTOs.
 * <p>
 * Enriches the response with reaction data, including the current user's reaction
 * and counts of likes and dislikes for each post.
 */
@Component
@RequiredArgsConstructor
public class BlogPostAssembler {

    private final BlogPostMapper mapper;
    private final ReactionService reactionService;
    private final SecurityUtils securityUtils;

    /**
     * Maps and enriches a single {@link BlogPost} entity to a {@link BlogPostResponse} DTO.
     * <p>
     * Sets the authenticated user's reaction and the total like/dislike counts.
     *
     * @param blogPost the blog post entity to map
     * @return the enriched {@link BlogPostResponse}
     */
    public BlogPostResponse mapAndEnrichToBlogPostResponse(BlogPost blogPost) {
        BlogPostResponse blogPostResponse = mapper.toResponse(blogPost);
        long blogPostId = blogPost.getId();

        blogPostResponse.setUserReaction(
                reactionService.getUserReactionTypeForPost(
                        securityUtils.getAuthenticatedUser().getId(),
                        blogPostId
                )
        );

        Map<ReactionType, Long> reactionCounts = reactionService.getReactionCountsByPostId(blogPostId);
        blogPostResponse.setLikeCount(reactionCounts.getOrDefault(ReactionType.LIKE, 0L));
        blogPostResponse.setDislikeCount(reactionCounts.getOrDefault(ReactionType.DISLIKE, 0L));
        return blogPostResponse;
    }

    /**
     * Maps and enriches a paginated {@link BlogPost} page to a page of {@link BlogPostResponse} DTOs.
     * <p>
     * Fetches user reactions and reaction counts for all posts in the page.
     *
     * @param page the paginated blog posts
     * @return a page of enriched {@link BlogPostResponse} DTOs
     */
    public Page<BlogPostResponse> mapAndEnrichToBlogPostResponses(Page<BlogPost> page) {
        List<Long> blogPostIds = page.getContent().stream()
                .map(BlogPost::getId)
                .toList();

        Long userId = securityUtils.getAuthenticatedUser().getId();
        Map<Long, ReactionType> userReactions = reactionService.getUserReactionTypesForPosts(userId, blogPostIds);
        Map<Long, Map<ReactionType, Long>> reactionsCounts = reactionService.getReactionCountsForPostIds(blogPostIds);

        return page.map(blogPost -> {
            BlogPostResponse blogPostResponse = mapper.toResponse(blogPost);
            Long postId = blogPost.getId();

            blogPostResponse.setUserReaction(userReactions.get(postId));

            Map<ReactionType, Long> counts = reactionsCounts.getOrDefault(postId, Map.of());
            blogPostResponse.setLikeCount(counts.getOrDefault(ReactionType.LIKE, 0L));
            blogPostResponse.setDislikeCount(counts.getOrDefault(ReactionType.DISLIKE, 0L));

            return blogPostResponse;
        });
    }

}
