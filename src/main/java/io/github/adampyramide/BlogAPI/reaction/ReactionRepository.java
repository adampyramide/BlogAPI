package io.github.adampyramide.BlogAPI.reaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, ReactionId> {

    Page<Reaction> findAllByPostId(Long postId, Pageable pageable);

    Page<Reaction> findAllByPostIdAndReactionType(Long postId, ReactionType reactionType, Pageable pageable);

    List<Reaction> findByAuthorIdAndPostIdIn(Long userId, List<Long> postIds);

    @Query("""
        SELECT r.reactionType, COUNT(r)
        FROM Reaction r
        WHERE r.post.id = :postId
        GROUP BY r.reactionType
    """)
    List<Object[]> countReactionsByPostId(@Param("postId") Long postId);

    @Query("""
        SELECT r.post.id, r.reactionType, COUNT(r)
        FROM Reaction r
        WHERE r.post.id IN :postIds
        GROUP BY r.post.id, r.reactionType
    """)
    List<Object[]> countReactionsGroupedByPostIds(@Param("postIds") List<Long> postIds);
}
