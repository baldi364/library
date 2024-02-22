package com.nico.library.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SigninRequest
{
    @NotBlank
    @Size(min = 4, max = 15)
    private String username;
    @NotBlank @Size(min = 6, max = 10)
    private String password;
}
