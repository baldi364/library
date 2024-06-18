package com.nico.library.dto.mapper;

import com.nico.library.dto.request.authentication.SignupRequest;
import com.nico.library.dto.response.user.UserResponse;
import com.nico.library.dto.response.user.UserSignUpResponse;
import com.nico.library.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User signUpAsEntity(SignupRequest request);
    UserSignUpResponse signUpAsResponse(User user);
    @Mapping(source = "userId", target = "id")
    @Mapping(target = "authority", expression = "java(user.getAuthorities().toString())")
    UserResponse asUserDetailsResponse (User user);
}
