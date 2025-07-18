package io.github.adampyramide.BlogAPI.reaction;

import io.github.adampyramide.BlogAPI.blogpost.BlogPostValidator;
import io.github.adampyramide.BlogAPI.exception.CustomException;
import io.github.adampyramide.BlogAPI.security.SecurityUtils;
import io.github.adampyramide.BlogAPI.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReactionService {

    ReactionRepository repo;
    ReactionMapper mapper;

    BlogPostValidator blogPostValidator;
    SecurityUtils securityUtils;

    public ReactionService(ReactionRepository repo, ReactionMapper mapper, BlogPostValidator blogPostValidator, SecurityUtils securityUtils) {
        this.repo = repo;
        this.mapper = mapper;
        this.blogPostValidator = blogPostValidator;
        this.securityUtils = securityUtils;
    }

    // ====================
    // Public methods
    // ====================

    public Page<ReactionResponseDTO> getReactionsByPostId(Long postId, ReactionType reactionType, Pageable pageable) {
        blogPostValidator.getByIdOrThrow(postId);

        Page<Reaction> reactions;

        if (reactionType == null)
            reactions = repo.findAllByPost_Id(postId, pageable);
        else
            reactions = repo.findAllByPost_IdAndReactionType(postId, reactionType, pageable);

        return reactions.map(mapper::toResponseDTO);
    }

    public void addReactionByPostId(Long postId, ReactionRequestDTO reactionDTO) {
        User user = securityUtils.getAuthenticatedUser();
        ReactionId reactionId = new ReactionId(
                user.getId(),
                postId
        );

        Reaction reaction = mapper.toEntity(reactionDTO);
        reaction.setId(reactionId);
        reaction.setAuthor(user);
        reaction.setPost(blogPostValidator.getByIdOrThrow(postId));

        repo.save(reaction);
    }

    public void deleteReactionByPostId(Long postId) {
        blogPostValidator.getByIdOrThrow(postId);

        ReactionId reactionId = new ReactionId(
                securityUtils.getAuthenticatedUser().getId(),
                postId
        );

        if (!repo.existsById(reactionId))
            throw new CustomException("Reaction not found", HttpStatus.NOT_FOUND);

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
