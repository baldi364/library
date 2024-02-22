package com.nico.library.controller;

import com.nico.library.service.AuthenticationService;
import com.nico.library.service.UserService;
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
    private final UserService userService;

    //Attivare l'utente
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/activate/{jwt}")
    public ResponseEntity<?> activate(@PathVariable @NotBlank String jwt)
    {
        return userService.activate(jwt);
    }

    //Ottenere l'username di un utente
    @GetMapping("/get-username/{userId}")
    public ResponseEntity<?> getUsername(@PathVariable @Min(1) int userId)
    {
        return userService.findUsername(userId);
    }

    @GetMapping("get-me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetails userDetails)
    {
        return userService.getMe(userDetails);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update_auths/{userId}")
    public ResponseEntity<?> updateAuths(@PathVariable @Min(1) int userId,
                                         @RequestBody @NotEmpty Set<String> authorities)
    {
        return userService.updateAuthorities(userId, authorities);
    }
}
