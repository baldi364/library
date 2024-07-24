package com.nico.library.controller;

import com.nico.library.dto.request.authentication.SigninRequest;
import com.nico.library.dto.request.authentication.SignupRequest;
import com.nico.library.dto.response.authentication.AuthenticationResponse;
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
            summary = "POST endpoint to register a user",
            description = "Register a new user by providing valid fields. The request must contain a unique username and email, along with other required details.",
            responses = {
                    @ApiResponse(
                            description = "User successfully created",
                            responseCode = "201",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSignUpResponse.class))
                    ),
                    @ApiResponse(
                            description = "Bad Request - Username or email already in use",
                            responseCode = "400",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "Internal Server Error",
                            responseCode = "500",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponse> signUp(@RequestBody @Valid SignupRequest request) {
        UserSignUpResponse response = authenticationServiceImpl.signUp(request);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(
            summary = "POST endpoint for user signin",
            description = "Authenticate a user by providing a valid username and password. On successful authentication, a JWT token is returned.",
            responses = {
                    @ApiResponse(
                            description = "User successfully authenticated",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class))
                    ),
                    @ApiResponse(
                            description = "Unauthorized - Bad Credentials",
                            responseCode = "401",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid SigninRequest request) {
        return authenticationServiceImpl.signIn(request);
    }
}
