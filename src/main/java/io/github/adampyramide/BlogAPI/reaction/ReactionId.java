package io.github.adampyramide.BlogAPI.reaction;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionId implements Serializable {

    private Long authorId;
    private Long postId;

}
