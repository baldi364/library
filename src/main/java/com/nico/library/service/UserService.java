package com.nico.library.service;

import com.nico.library.dto.response.user.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface UserService {
    void activate(String jwt);
    String findUsername(int userId);
    void updateAuthorities(int id, Set<String> authorities);
    UserResponse getMe(UserDetails userDetails);
}
