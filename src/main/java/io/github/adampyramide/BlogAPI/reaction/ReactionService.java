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

    // ====================
    // Public methods
    // ====================

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

    // ====================
    // Internal methods
    // ====================

    public ReactionType getUserReactionTypeForPost(Long userId, Long postId) {
        ReactionId reactionId = new ReactionId(userId, postId);

        return repo.findById(reactionId)
                .map(Reaction::getReactionType)
                .orElse(null);
    }

    public Map<Long, ReactionType> getUserReactionTypesForPosts(Long userId, List<Long> postIds) {
        List<Reaction> reactions = repo.findByAuthorIdAndPostIdIn(userId, postIds);

        return reactions.stream().collect(Collectors.toMap(
                reaction -> reaction.getPost().getId(),
                Reaction::getReactionType
        ));
    }

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

}
