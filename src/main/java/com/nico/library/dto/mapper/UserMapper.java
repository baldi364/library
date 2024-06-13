package com.nico.library.dto.mapper;

import com.nico.library.dto.request.authentication.SignupRequest;
import com.nico.library.dto.response.user.UserSignUpResponse;
import com.nico.library.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User asEntity (SignupRequest request);

    UserSignUpResponse asResponse(User user);
}
