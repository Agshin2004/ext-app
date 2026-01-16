package com.agshin.extapp.mappers;

import com.agshin.extapp.model.entities.User;
import com.agshin.extapp.model.request.user.CreateUserRequest;
import com.agshin.extapp.model.response.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "Spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE // do not override existing values with null
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(CreateUserRequest request);

    void updateEntity(CreateUserRequest request, @MappingTarget User user);

//    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "jwt", source = "jwt")
    UserResponse toResponse(User user, String jwt);
}
