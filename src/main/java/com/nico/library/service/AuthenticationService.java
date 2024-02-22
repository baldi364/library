package com.nico.library.service;

import com.nico.library.entity.Authority;
import com.nico.library.entity.User;
import com.nico.library.payload.request.SigninRequest;
import com.nico.library.payload.request.SignupRequest;
import com.nico.library.payload.response.AuthenticationResponse;
import com.nico.library.repository.AuthorityRepository;
import com.nico.library.repository.UserRepository;
import com.nico.library.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService
{
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    //Registrazione utente
    public ResponseEntity<String> signUp(SignupRequest request)
    {
        if (userRepository.existsByEmailOrUsername(request.getEmail(), request.getUsername()))
        {
            return new ResponseEntity("Username or Email already in use", HttpStatus.BAD_REQUEST);
        }

        Authority a = authorityRepository.findByDefaultAuthorityTrue();

        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .authorities(Collections.singleton(a))
                .build();

        userRepository.save(user);
        return new ResponseEntity<String>("User successfully registered",HttpStatus.CREATED);
    }

    //Accesso utente
    public ResponseEntity<?> signIn(SigninRequest request)
    {
        //cerco l'utente
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        //confronto le password
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new BadCredentialsException("Bad credentials");
        }

        if(!user.isEnabled())
        {
            user.setEnabled(true);
        }

        String jwtToken = jwtService.generateToken(user, user.getUserId());

        return new ResponseEntity(AuthenticationResponse.builder()
                .id(user.getUserId())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(authorities(user.getAuthorities()))
                .token(jwtToken)
                .build(),
                HttpStatus.OK);
    }

    private String[] authorities(Collection<? extends GrantedAuthority> auths)
    {
        return auths.stream()
                .map(a -> a.getAuthority())
                .toArray(String[]::new);
    }


}
