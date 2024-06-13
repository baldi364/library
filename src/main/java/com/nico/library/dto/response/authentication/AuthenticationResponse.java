package com.nico.library.dto.response.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse
{
    private int id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String[] authorities;
    private String token;

}
