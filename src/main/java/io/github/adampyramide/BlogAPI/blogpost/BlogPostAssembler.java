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

@Component
@RequiredArgsConstructor
public class BlogPostAssembler {

    private final BlogPostMapper mapper;

    private final ReactionService reactionService;
    private final SecurityUtils securityUtils;

    // ====================
    // Public methods
    // ====================

    public BlogPostResponse mapToBlogPostResponse(BlogPost blogPost) {
        BlogPostResponse blogPostResponse = mapper.toResponse(blogPost);
        long BlogPostId = blogPostResponse.getId();

        blogPostResponse.setUserReaction(
                reactionService.getUserReactionTypeForPost(
                        securityUtils.getAuthenticatedUser().getId(),
                        BlogPostId
                )
        );

        Map<ReactionType, Long> reactionCounts = reactionService.getReactionCountsByPostId(BlogPostId);
        blogPostResponse.setLikeCount(reactionCounts.getOrDefault(ReactionType.LIKE, 0L));
        blogPostResponse.setDislikeCount(reactionCounts.getOrDefault(ReactionType.DISLIKE, 0L));
        return blogPostResponse;
    }

    public Page<BlogPostResponse> mapToBlogPostResponses(Page<BlogPost> page) {
        List<Long> postIds = page.getContent().stream()
                .map(BlogPost::getId)
                .toList();

        Long userId = securityUtils.getAuthenticatedUser().getId();
        Map<Long, ReactionType> userReactions = reactionService.getUserReactionTypesForPosts(userId, postIds);
        Map<Long, Map<ReactionType, Long>> reactionsCounts = reactionService.getReactionCountsForPostIds(postIds);

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
