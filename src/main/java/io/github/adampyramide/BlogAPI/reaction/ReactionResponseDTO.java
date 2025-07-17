package io.github.adampyramide.BlogAPI.reaction;

import io.github.adampyramide.BlogAPI.user.PublicUserDTO;

public record ReactionResponseDTO(

        PublicUserDTO author,
        ReactionType reactionType

) {}