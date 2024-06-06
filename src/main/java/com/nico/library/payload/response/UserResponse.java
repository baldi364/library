package com.nico.library.payload.response;

import com.nico.library.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse
{
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String authority;

    public static UserResponse fromUserDetailsToUserResponse(User user)
    {
        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getSurname(),
                user.getUsername(),
                user.getEmail(),
                user.getAuthorities().toString()
        );
    }
}
