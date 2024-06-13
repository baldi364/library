package com.nico.library.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignUpResponse {
    private String name;
    private String surname;
    private String username;
    private String email;
}
