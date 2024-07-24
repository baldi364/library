package com.nico.library.controller;

import com.nico.library.dto.response.user.UserResponse;
import com.nico.library.service.implementation.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/user")
public class UserController
{
    private final UserServiceImpl userServiceImpl;

    @Operation(
            summary = "PUT endpoint to enable a user",
            description = "Enables a user account by providing a valid JWT token. This action can only be performed by an admin.",
            responses = {
                    @ApiResponse(
                            description = "User successfully enabled",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized - Admin privileges required",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "User not found - Invalid JWT token provided",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500"
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/activate/{jwt}")
    public ResponseEntity<String> activate(
            @PathVariable("jwt") @NotBlank String jwt)
    {
        userServiceImpl.activate(jwt);
        return new ResponseEntity<>("Your account is now enabled!", OK);
    }

    @Operation(
            summary = "GET endpoint to retrieve username by user ID",
            description = "Fetches the username corresponding to the provided user ID. If the user is not found, a 404 error is returned.",
            responses = {
                    @ApiResponse(
                            description = "Username successfully found",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500"
                    )
            }
    )
    @GetMapping("/get-username/{userId}")
    public ResponseEntity<String> getUsername(
            @PathVariable("userId") @Min(1) int userId)
    {
        String username = userServiceImpl.findUsername(userId);
        return new ResponseEntity<>(String.format("Username: %s", username), OK);
    }

    @Operation(
            summary = "GET endpoint to retrieve your details",
            description = "Returns your user details such as username, email and so on. If the user is not logged, a 400 error is returned.",
            responses = {
                    @ApiResponse(
                            description = "All user details",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            description = "User not logged",
                            responseCode = "400",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @GetMapping("get-me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserDetails userDetails)
    {
        UserResponse response = userServiceImpl.getMe(userDetails);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "PUT endpoint to update user authorities",
            description = "Sets the new authorities to the specified user. This action can only be performed by an admin.",
            responses = {
                    @ApiResponse(
                            description = "New authorities successfully updated",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "No authorities found",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500"
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update_auths/{userId}")
    public ResponseEntity<String> updateAuths(
            @PathVariable("userId") @Min(1) int userId,
            @RequestBody @NotEmpty Set<String> authorities)
    {
        userServiceImpl.updateAuthorities(userId, authorities);
        return new ResponseEntity<>(String.format("Authorities updated for user with id %d", userId), OK);
    }
}