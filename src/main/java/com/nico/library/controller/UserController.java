package com.nico.library.controller;

import com.nico.library.service.implementation.UserServiceImpl;
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

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/user")
public class UserController
{
    private final UserServiceImpl userServiceImpl;

    //Attivare l'utente
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/activate/{jwt}")
    public ResponseEntity<?> activate(@PathVariable("jwt") @NotBlank String jwt)
    {
        return userServiceImpl.activate(jwt);
    }

    //Ottenere l'username di un utente
    @GetMapping("/get-username/{userId}")
    public ResponseEntity<?> getUsername(@PathVariable("userId") @Min(1) int userId)
    {
        return userServiceImpl.findUsername(userId);
    }

    @GetMapping("get-me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetails userDetails)
    {
        return userServiceImpl.getMe(userDetails);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update_auths/{userId}")
    public ResponseEntity<?> updateAuths(@PathVariable("userId") @Min(1) int userId,
                                         @RequestBody @NotEmpty Set<String> authorities)
    {
        return userServiceImpl.updateAuthorities(userId, authorities);
    }
}
