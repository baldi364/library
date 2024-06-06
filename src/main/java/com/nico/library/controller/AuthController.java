package com.nico.library.controller;

import com.nico.library.payload.request.SigninRequest;
import com.nico.library.payload.request.SignupRequest;
import com.nico.library.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final AuthenticationService authenticationService;

    //Registrazione utente
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignupRequest request) {
        return authenticationService.signUp(request);
    }

    //Accesso utente
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid SigninRequest request) {
        return authenticationService.signIn(request);
    }
}
