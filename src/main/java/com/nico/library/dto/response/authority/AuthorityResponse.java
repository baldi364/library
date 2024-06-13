package com.nico.library.dto.response.authority;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorityResponse {
    private Long authorityId;
    private String authorityName;
    private boolean visible;
    private boolean defaultAuthority;
}
