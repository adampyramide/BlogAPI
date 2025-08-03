package io.github.adampyramide.BlogAPI.reaction.dto;

import io.github.adampyramide.BlogAPI.reaction.ReactionType;
import io.github.adampyramide.BlogAPI.user.dto.PublicUserResponse;

public record ReactionResponse(

        PublicUserResponse author,
        ReactionType reactionType

) {}