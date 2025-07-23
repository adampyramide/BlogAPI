package io.github.adampyramide.BlogAPI.reaction;

import io.github.adampyramide.BlogAPI.blogpost.BlogPost;
import io.github.adampyramide.BlogAPI.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {

    @EmbeddedId
    private ReactionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private BlogPost post;

    @CreationTimestamp
    private LocalDateTime createTime;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

}
