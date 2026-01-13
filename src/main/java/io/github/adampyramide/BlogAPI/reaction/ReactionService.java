package io.github.adampyramide.BlogAPI.reaction;

import io.github.adampyramide.BlogAPI.blogpost.BlogPostQueryService;
import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.reaction.dto.ReactionRequest;
import io.github.adampyramide.BlogAPI.reaction.dto.ReactionResponse;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository repo;
    private final ReactionMapper mapper;
    private final BlogPostQueryService blogPostQueryService;
    private final SecurityUtils securityUtils;

    /**
     * Retrieves the reaction type of a specific user for a given blog post.
     *
     * @param userId the ID of the user
     * @param postId the ID of the blog post
     * @return the {@link ReactionType} if a reaction exists, or {@code null} otherwise
     */
    public ReactionType getUserReactionTypeForPost(Long userId, Long postId) {
        ReactionId reactionId = new ReactionId(userId, postId);

        return repo.findById(reactionId)
                .map(Reaction::getReactionType)
                .orElse(null);
    }

    /**
     * Retrieves reaction types of a specific user for multiple blog posts.
     *
     * @param userId the ID of the user
     * @param postIds the IDs of the blog posts
     * @return a map where each post ID maps to the {@link ReactionType} the user applied;
     * only includes posts the user has reacted to
     */
    public Map<Long, ReactionType> getUserReactionTypesForPosts(Long userId, List<Long> postIds) {
        List<Reaction> reactions = repo.findByAuthorIdAndPostIdIn(userId, postIds);

        return reactions.stream().collect(Collectors.toMap(
                reaction -> reaction.getPost().getId(),
                Reaction::getReactionType
        ));
    }

    /**
     * Retrieves reaction counts grouped by reaction type for multiple blog posts.
     *
     * @param postIds the IDs of the blog posts
     * @return a map where each post ID maps to a reaction type/count map
     */
    public Map<Long, Map<ReactionType, Long>> getReactionCountsForPostIds(List<Long> postIds) {
        List<Object[]> results = repo.countReactionsGroupedByPostIds(postIds);

        Map<Long, Map<ReactionType, Long>> reactionMap = new HashMap<>();
        for (Object[] row : results) {
            Long postId = (Long) row[0];
            ReactionType type = (ReactionType) row[1];
            Long count = (Long) row[2];

            reactionMap
                    .computeIfAbsent(postId, k -> new HashMap<>())
                    .put(type, count);
        }

        return reactionMap;
    }

    /**
     * Retrieves reaction counts grouped by reaction type for a single blog post.
     *
     * @param postId the ID of the blog post
     * @return a map of {@link ReactionType} to reaction count, including zero counts
     */
    public Map<ReactionType, Long> getReactionCountsByPostId(Long postId) {
        Map<ReactionType, Long> counts = new EnumMap<>(ReactionType.class);
        for (Object[] row : repo.countReactionsByPostId(postId)) {
            counts.put((ReactionType) row[0], (Long) row[1]);
        }
        for (ReactionType type : ReactionType.values()) {
            counts.putIfAbsent(type, 0L);
        }

        return counts;
    }

    /**
     * Retrieves {@link ReactionResponse}'s, optionally filtered by reaction type.
     *
     * @param postId the ID of the blog post
     * @param reactionType the reaction type to filter by, or {@code null} to retrieve all reactions
     * @param pageable pagination and sorting information
     * @return a page of {@link ReactionResponse} for the specified blog post
     * @throws ApiException if the blog post does not exist
     */
    public Page<ReactionResponse> getReactionsByPostId(Long postId, ReactionType reactionType, Pageable pageable) {
        blogPostQueryService.getByIdOrThrow(postId);

        Page<Reaction> reactions;
        if (reactionType == null) {
            reactions = repo.findAllByPostId(postId, pageable);
        }
        else {
            reactions = repo.findAllByPostIdAndReactionType(postId, reactionType, pageable);
        }

        return reactions.map(mapper::toResponse);
    }

    /**
     * Adds a reaction to a blog post on behalf of the currently authenticated user.
     *
     * @param postId the ID of the blog post to react to
     * @param reactionRequest the reaction data to apply
     * @throws ApiException if the blog post does not exist
     */
    public void addReactionByPostId(Long postId, ReactionRequest reactionRequest) {
        User user = securityUtils.getAuthenticatedUser();
        ReactionId reactionId = new ReactionId(
                user.getId(),
                postId
        );

        Reaction reaction = Reaction.builder()
                .id(reactionId)
                .author(user)
                .post(blogPostQueryService.getByIdOrThrow(postId))
                .reactionType(reactionRequest.reactionType())
                .build();

        repo.save(reaction);
    }

    /**
     * Removes the authenticated user's reaction from a blog post by post ID
     *
     * @param postId the ID of the blog post
     * @throws ApiException if the blog post does not exist or the reaction is not found
     */
    public void deleteReactionByPostId(Long postId) {
        blogPostQueryService.getByIdOrThrow(postId);

        Long userId = securityUtils.getAuthenticatedUser().getId();
        ReactionId reactionId = new ReactionId(
                userId,
                postId
        );

        if (!repo.existsById(reactionId)) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "REACTION_NOT_FOUND",
                    "Reaction with post ID %d and user ID %d not found."
                            .formatted(postId, userId)
            );
        }

        repo.deleteById(reactionId);
    }

}
