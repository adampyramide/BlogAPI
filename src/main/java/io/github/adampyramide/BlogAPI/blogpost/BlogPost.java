package io.github.adampyramide.BlogAPI.blogpost;

import io.github.adampyramide.BlogAPI.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String body;
    private LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

}
