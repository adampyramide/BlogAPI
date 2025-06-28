package io.github.adampyramide.BlogAPI.post;

import io.github.adampyramide.BlogAPI.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;
    private String content;
    private LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

}
