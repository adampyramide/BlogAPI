package io.github.adampyramide.BlogAPI.reaction.dto;

import io.github.adampyramide.BlogAPI.reaction.ReactionType;
import io.github.adampyramide.BlogAPI.user.dto.UserPreviewResponse;

public record ReactionResponse(

        UserPreviewResponse author,
        ReactionType reactionType

) {}