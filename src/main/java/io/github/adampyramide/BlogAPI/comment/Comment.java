package io.github.adampyramide.BlogAPI.comment;

import io.github.adampyramide.BlogAPI.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long postId;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private String body;

}
