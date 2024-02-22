package com.nico.library.service;

import com.nico.library.entity.Authority;
import com.nico.library.entity.User;
import com.nico.library.exception.ResourceNotFoundException;
import com.nico.library.payload.response.UserResponse;
import com.nico.library.repository.AuthorityRepository;
import com.nico.library.repository.UserRepository;
import com.nico.library.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final JwtService jwtService;

    @Transactional
    public ResponseEntity<?> activate(String jwt)
    {
        String username = jwtService.extractUsername(jwt);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        user.setEnabled(true);
        return new ResponseEntity("Welcome " + username + ", your account is activated", HttpStatus.OK);
    }

    public ResponseEntity<?> findUsername(int userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User", "userId", userId));

        String username = user.getUsername();
        return new ResponseEntity<>("Username: " + username, HttpStatus.OK);
    }

    public ResponseEntity<?> updateAuthorities(int id, Set<String> authorities)
    {
        // Verifico l'esistenza dell'utente
        User u = userRepository.findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("User", "id", id)));

        //Trasformo Set<String> in Set<Authority>
        Set<Authority> auths = authorityRepository.findByVisibleTrueAndAuthorityNameIn(authorities);
        if(auths.isEmpty())
        {
            return new ResponseEntity("Authorities not found", HttpStatus.NOT_FOUND);
        }

        //Setto il Set<Authority> su user e salvare
        u.setAuthorities(auths);
        userRepository.save(u);
        return new ResponseEntity("Authorities updated for user " + u.getUsername(), HttpStatus.OK);
    }

    public ResponseEntity<?> getMe(UserDetails userDetails)
    {
        UserResponse u = UserResponse.fromUserDetailsToUserResponse((User) userDetails);
        return new ResponseEntity(u,HttpStatus.OK);
    }
}
