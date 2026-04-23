package com.agshin.extapp.features.user.infrastructure;

import com.agshin.extapp.features.user.api.dto.CreateUserRequest;
import com.agshin.extapp.features.user.api.dto.UserDto;
import com.agshin.extapp.features.user.api.dto.UserResponse;
import com.agshin.extapp.features.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "Spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE // do not override existing values with null
)
public interface UserMapper {
    @Mapping(target = "password", source = "encodedPassword")
    User toEntity(String email, String username, String encodedPassword);

    void updateEntity(CreateUserRequest request, @MappingTarget User user);

    UserDto toDto(User user);

//    @Mapping(target = "role", expression = "java(user.getRole().categoryName())")
    @Mapping(target = "jwt", source = "jwt")
    UserResponse toResponse(User user, String jwt);
}
