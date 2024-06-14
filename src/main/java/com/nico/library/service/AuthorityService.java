package com.nico.library.service;

import com.nico.library.dto.request.authority.AuthorityRequest;
import com.nico.library.dto.response.authority.AuthorityResponse;

public interface AuthorityService {

    AuthorityResponse addAuthority(AuthorityRequest request);
    void switchVisibility(byte id);
}
