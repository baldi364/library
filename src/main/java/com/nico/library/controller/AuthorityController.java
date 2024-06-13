package com.nico.library.controller;

import com.nico.library.dto.request.authority.AuthorityRequest;
import com.nico.library.dto.response.authority.AuthorityResponse;
import com.nico.library.service.implementation.AuthorityServiceImpl;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-authority")
    public ResponseEntity<AuthorityResponse> addAuthority(@RequestBody AuthorityRequest request)
    {
        AuthorityResponse response = authorityServiceImpl.addAuthority(request);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{authorityId}")
    public ResponseEntity<?> switchVisibility(@PathVariable("authorityId") @Min(1) byte authorityId)
    {
        return authorityServiceImpl.switchVisibility(authorityId);
    }
}
