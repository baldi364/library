package com.nico.library.controller;

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
    public ResponseEntity<?> addAuthority(@RequestParam @Size(max = 30, min = 7) @NotEmpty String newAuthority)
    {
        return authorityServiceImpl.addAuthority(newAuthority);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{authorityId}")
    public ResponseEntity<?> switchVisibility(@PathVariable("authorityId") @Min(1) byte authorityId)
    {
        return authorityServiceImpl.switchVisibility(authorityId);
    }
}
