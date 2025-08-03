package io.github.adampyramide.BlogAPI.reaction;

import io.github.adampyramide.BlogAPI.reaction.dto.ReactionResponse;
import io.github.adampyramide.BlogAPI.user.UserMapper;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class
)
public interface ReactionMapper {

    ReactionResponse toResponse(Reaction entity);

}
