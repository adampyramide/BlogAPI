package io.github.adampyramide.BlogAPI.reaction.dto;

import io.github.adampyramide.BlogAPI.reaction.ReactionType;

public record ReactionRequest(

        ReactionType reactionType

) {}
