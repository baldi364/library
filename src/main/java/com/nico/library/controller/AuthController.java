package com.nico.library.controller;

import com.nico.library.dto.request.authentication.SigninRequest;
import com.nico.library.dto.request.authentication.SignupRequest;
import com.nico.library.dto.response.user.UserSignUpResponse;
import com.nico.library.service.implementation.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auths")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    //Registrazione utente
    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponse> signUp(@RequestBody @Valid SignupRequest request) {
        UserSignUpResponse response = authenticationServiceImpl.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Accesso utente
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid SigninRequest request) {
        return authenticationServiceImpl.signIn(request);
    }
}
