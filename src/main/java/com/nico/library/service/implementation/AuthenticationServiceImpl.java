package com.nico.library.service.implementation;

import com.nico.library.dto.mapper.UserMapper;
import com.nico.library.dto.response.user.UserSignUpResponse;
import com.nico.library.entity.Authority;
import com.nico.library.entity.User;
import com.nico.library.dto.request.authentication.SigninRequest;
import com.nico.library.dto.request.authentication.SignupRequest;
import com.nico.library.dto.response.authentication.AuthenticationResponse;
import com.nico.library.exceptions.custom.BadCredentialsException;
import com.nico.library.exceptions.custom.BadRequestException;
import com.nico.library.repository.AuthorityRepository;
import com.nico.library.repository.UserRepository;
import com.nico.library.security.JwtService;
import com.nico.library.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService
{
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    //User Registration

    /**
     * Manages the process of registering a new user in the application.
     *
     * @param request SignupRequest object containing the information of the new user to register.
     * @return the mapped UserDTO information of the registered user.
     * @throws BadRequestException If the username or email is already in use.
     */
    public UserSignUpResponse signUp(SignupRequest request)
    {
        // Control if email and username are already in use
        if (userRepository.existsByEmailOrUsername(request.getEmail(), request.getUsername()))
        {
            throw new BadRequestException("Username or Email already in use");
        }

        // Retrieves the default authority for new users
        Authority a = authorityRepository.findByDefaultAuthorityTrue();

        // Build a new User with the information provided in the request
        User user = userMapper.signUpAsEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAuthorities(Collections.singleton(a));


        userRepository.save(user);
        return userMapper.signUpAsResponse(user);
    }

    //User Signin

    /**
     * Manages the user login process to the application.
     *
     * @param request SigninRequest containing the user's login credentials.
     * @return ResponseEntity containing an AuthenticationResponse object with user information and the generated JWT token.
     * @throws BadCredentialsException if the credentials provided do not match a valid user.
     */
    public ResponseEntity<?> signIn(SigninRequest request)
    {
        // Search for the user in the repository by the username provided
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(BadCredentialsException::new);

        // Compare the two passwords
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new BadCredentialsException();
        }

        if(!user.isEnabled())
        {
            user.setEnabled(true);
        }

        // Generate a new JWT token
        String jwtToken = jwtService.generateToken(user, Math.toIntExact(user.getUserId()));

        return new ResponseEntity<>(AuthenticationResponse.builder()
                .id(Math.toIntExact(user.getUserId()))
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(authorities(user.getAuthorities()))
                .token(jwtToken)
                .build(),
                HttpStatus.OK);
    }

    /**
     * Extracts authentications strings from a collection of GrantedAuthority objects.
     *
     * @param auths A collection of GrantedAuthority objects that represent the user's permissions.
     * @return An array of Strings with the permissions extracted from the provided collection.
     *
     */
    private String[] authorities(Collection<? extends GrantedAuthority> auths)
    {
        return auths.stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }
}
