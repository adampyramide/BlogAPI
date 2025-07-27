package io.github.adampyramide.BlogAPI.reaction;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionMapper {

    ReactionResponse toResponse(Reaction entity);

}
