package com.nico.library.dto.response.user;

import com.nico.library.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
