package io.github.adampyramide.BlogAPI.reaction;

import io.github.adampyramide.BlogAPI.reaction.dto.ReactionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionMapper {

    ReactionResponse toResponse(Reaction entity);

}
