package io.github.adampyramide.BlogAPI.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User authDTOToEntity(AuthUserDTO dto);

    PublicUserDTO toPublicDTO(User user);

}
