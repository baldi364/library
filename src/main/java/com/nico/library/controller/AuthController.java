package com.nico.library.controller;

import com.nico.library.dto.request.authentication.SigninRequest;
import com.nico.library.dto.request.authentication.SignupRequest;
import com.nico.library.dto.response.user.UserSignUpResponse;
import com.nico.library.service.implementation.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/auths")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    @Operation(
            summary = "POST ENDPOINT FOR USER SIGNUP",
            description = "Register a new user",
            responses = {
                    @ApiResponse(
                            description = "User successfully created",
                            responseCode = "201",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSignUpResponse.class))
                    ),
                    @ApiResponse(
                            description = "Bad Request - Username or email already in use",
                            responseCode = "400"),
                    @ApiResponse(
                            description = "Something went wrong",
                            responseCode = "500"
                    )
            }
    )
    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponse> signUp(@RequestBody @Valid SignupRequest request) {
        UserSignUpResponse response = authenticationServiceImpl.signUp(request);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(
            summary = "POST ENDPOINT FOR USER SIGNIN",
            description = "Authenticate a user and provide a JWT token.",
            responses = {
                    @ApiResponse(
                            description = "User successfully authenticated",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Unauthorized - Bad Credentials",
                            responseCode = "401"),
                    @ApiResponse(
                            description = "Something went wrong",
                            responseCode = "500"
                    )
            }
    )
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid SigninRequest request) {
        return authenticationServiceImpl.signIn(request);
    }
}
