package com.nico.library.service;

import com.nico.library.dto.request.authentication.SignupRequest;
import com.nico.library.dto.response.user.UserSignUpResponse;

public interface AuthenticationService {
    UserSignUpResponse signUp(SignupRequest request);
}

