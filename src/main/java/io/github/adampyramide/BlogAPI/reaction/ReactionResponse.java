package io.github.adampyramide.BlogAPI.reaction;

import io.github.adampyramide.BlogAPI.user.PublicUserResponse;

public record ReactionResponse(

        PublicUserResponse author,
        ReactionType reactionType

) {}