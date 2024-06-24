package com.nico.library.controller;

import com.nico.library.dto.request.authority.AuthorityRequest;
import com.nico.library.dto.response.authority.AuthorityResponse;
import com.nico.library.service.implementation.AuthorityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ath")
@RequiredArgsConstructor
@Validated
public class AuthorityController
{
    private final AuthorityServiceImpl authorityServiceImpl;

    @Operation(
            summary = "POST endpoint to add an authority",
            description = "Authority name must be unique and starts with 'ROLE_'. This action can only be performed by an admin",
            responses = {
                    @ApiResponse(
                            description = "Authority successfully added",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "Bad Request - Invalid authority",
                            responseCode = "400",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "Something went wrong",
                            responseCode = "500",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-authority")
    public ResponseEntity<AuthorityResponse> addAuthority(@RequestBody @Valid AuthorityRequest request)
    {
        AuthorityResponse response = authorityServiceImpl.addAuthority(request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(
            summary = "PATCH endpoint to toggle authority visibility",
            description = "Toggle the authority visibility. This action can only be performed by an admin.",
            responses = {
                    @ApiResponse(
                            description = "Authority visibility successfully switched",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Authority not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Something went wrong",
                            responseCode = "500"
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{authorityId}")
    public ResponseEntity<String> switchVisibility(
            @PathVariable("authorityId") @Min(1) byte authorityId)
    {
        authorityServiceImpl.switchVisibility(authorityId);
        return ResponseEntity.ok(String.format("Authority with id '%d' updated", authorityId));
    }
}
