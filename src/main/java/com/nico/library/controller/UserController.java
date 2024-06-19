package com.nico.library.controller;

import com.nico.library.dto.response.user.UserResponse;
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

import static org.springframework.http.HttpStatus.OK;

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
    public ResponseEntity<String> activate(@PathVariable("jwt") @NotBlank String jwt)
    {
        userServiceImpl.activate(jwt);
        return new ResponseEntity<>("Your account is now enabled!", OK);
    }

    //Ottenere l'username di un utente
    @GetMapping("/get-username/{userId}")
    public ResponseEntity<String> getUsername(@PathVariable("userId") @Min(1) int userId)
    {
        String username = userServiceImpl.findUsername(userId);
        return new ResponseEntity<>(String.format("Username: %s",username), OK);
    }

    @GetMapping("get-me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserDetails userDetails)
    {
        UserResponse response = userServiceImpl.getMe(userDetails);
        return ResponseEntity.ok(response);
    }

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
