package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.auth.AuthRequest;
import io.github.adampyramide.BlogAPI.user.dto.UserPreviewResponse;
import io.github.adampyramide.BlogAPI.user.dto.UpdateUserRequest;
import io.github.adampyramide.BlogAPI.user.dto.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User authDTOToEntity(AuthRequest dto);

    UserPreviewResponse toUserPreviewResponse(User user);

    @Mapping(target = "id", ignore = true)
    void updateEntity(UpdateUserRequest request, @MappingTarget User entity);

}
