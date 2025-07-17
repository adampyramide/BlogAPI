package io.github.adampyramide.BlogAPI.reaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, ReactionId> {

    @Query("""
        SELECT r.reactionType, COUNT(r)
        FROM Reaction r
        WHERE r.post.id = :postId
        GROUP BY r.reactionType
    """)
    List<Object[]> countReactionsByPostIdGrouped(@Param("postId") Long postId);

//    @Query("""
//        SELECT r.post.id, r.reactionType, COUNT(r)
//        FROM Reaction r
//        WHERE r.post.id IN :postIds
//        GROUP BY r.post.id, r.reactionType
//    """)
//    List<Object[]> countReactionsGroupedByPost();
    @Query("""
            SELECT r.post.id, r.reactionType, COUNT(r)
            FROM Reaction r
            GROUP BY r.post.id, r.reactionType
        """)
    List<Object[]> countReactionsGroupedByPost();

    List<Reaction> findAllByPost_Id(Long postId);

    List<Reaction> findAllByPost_IdAndReactionType(Long postId, ReactionType reactionType);


}
