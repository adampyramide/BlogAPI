package io.github.adampyramide.BlogAPI.reaction;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionMapper {

    Reaction toEntity(ReactionRequestDTO requestDTO);

    ReactionResponseDTO toResponseDTO(Reaction entity);

}
