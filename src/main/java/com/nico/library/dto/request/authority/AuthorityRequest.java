package com.nico.library.dto.request.authority;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityRequest {

    @Column(length = 30, nullable = false, unique = true)
    private String authorityName;
}
