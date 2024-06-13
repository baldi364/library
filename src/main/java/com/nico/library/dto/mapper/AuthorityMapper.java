package com.nico.library.dto.mapper;

import com.nico.library.dto.request.authority.AuthorityRequest;
import com.nico.library.dto.response.authority.AuthorityResponse;
import com.nico.library.entity.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {

    AuthorityResponse asResponse(Authority authority);
    @Mapping(target = "authorityId", ignore = true)
    @Mapping(target = "visible", ignore = true)
    @Mapping(target = "defaultAuthority", ignore = true)
    Authority asEntity(AuthorityRequest request);
}
