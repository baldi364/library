package com.nico.library.service.implementation;

import com.nico.library.dto.mapper.UserMapper;
import com.nico.library.entity.Authority;
import com.nico.library.entity.User;
import com.nico.library.exceptions.custom.BadRequestException;
import com.nico.library.exceptions.custom.EmptyAuthoritiesException;
import com.nico.library.exceptions.custom.EmptyListException;
import com.nico.library.exceptions.custom.ResourceNotFoundException;
import com.nico.library.dto.response.user.UserResponse;
import com.nico.library.repository.AuthorityRepository;
import com.nico.library.repository.UserRepository;
import com.nico.library.security.JwtService;
import com.nico.library.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    /**
     * Enables the user's account corresponding to the provided JWT token.
     * <p>
     * This method extracts the username from the JWT token and enables the user's account
     * if the user is found. If no user is found with the extracted username, a
     * {@link ResourceNotFoundException} is thrown.
     * </p>
     *
     * @param jwt the JWT token used to identify the user.
     * @throws ResourceNotFoundException if no user with the extracted username is found.
     */
    @Transactional
    public void activate(String jwt) {

        // I extract the username from the JWT token
        String username = jwtService.extractUsername(jwt);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        user.setEnabled(true);
        userRepository.save(user);
    }

    /**
     * Finds the username corresponding to the provided user ID.
     * <p>
     * This method searches for a user by their unique ID and returns their username.
     * If the user is not found, a {@link ResourceNotFoundException} is thrown.
     * </p>
     *
     * @param userId the unique identifier of the user whose username you want to find.
     * @return the username of the user.
     * @throws ResourceNotFoundException if no user with the specified ID is found.
     */
    public String findUsername(int userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        return user.getUsername();
    }

    /**
     * Updates user's authorities with the new authorities provided.
     * <p>
     * This method searches for a user by their unique ID and sets the new authorities provided.
     * If the user is not found, a {@link ResourceNotFoundException} is thrown.
     * Also, an {@link EmptyListException} is thrown if no authorities are found.
     * </p>
     *
     * @param userId      the unique identifier of the user whose authorities you want to update.
     * @param authorities the set of new authorities to assign to the user.
     * @throws ResourceNotFoundException if no user with the specified ID is found.
     * @throws EmptyListException if no authorities are found.
     */
    @Transactional
    public void updateAuthorities(int userId, Set<String> authorities) {

        User u = userRepository.findById(userId)
                .orElseThrow((() -> new ResourceNotFoundException("User", "id", userId)));

        //Retrieve visible authorities find in the DB
        Set<Authority> auths = authorityRepository.findByVisibleTrueAndAuthorityNameIn(authorities);

        //Extract authority names found in auths
        Set<String> foundAuthorityNames =
                auths.stream()
                .map(Authority::getAuthorityName)
                .collect(Collectors.toSet());

        //Extract authorities not found in the DB
        Set<String> notFoundAuthorities =
                authorities.stream()
                .filter(auth -> !foundAuthorityNames.contains(auth))
                .collect(Collectors.toSet());

        if (!notFoundAuthorities.isEmpty()) {
            throw new EmptyAuthoritiesException(notFoundAuthorities);
        }

        u.setAuthorities(auths);
        userRepository.save(u);
    }

    /**
     * Retrieves the details of the current user.
     *
     * @param userDetails the details of the current user.
     * @return the details of the current user in a {@link UserResponse} object.
     * @throws BadRequestException if no user is logged in.
     */
    public UserResponse getMe(UserDetails userDetails) {

        if (userDetails == null) {
            throw new BadRequestException("No user logged! Please, log in and try again.");
        }

        // UserDetails response mapped
        return userMapper.asUserDetailsResponse((User) userDetails);
    }
}
