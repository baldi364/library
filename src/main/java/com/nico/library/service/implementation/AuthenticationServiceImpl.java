package com.nico.library.service.implementation;

import com.nico.library.dto.mapper.UserMapper;
import com.nico.library.dto.response.user.UserSignUpResponse;
import com.nico.library.entity.Authority;
import com.nico.library.entity.User;
import com.nico.library.dto.request.authentication.SigninRequest;
import com.nico.library.dto.request.authentication.SignupRequest;
import com.nico.library.dto.response.authentication.AuthenticationResponse;
import com.nico.library.exceptions.custom.BadRequestException;
import com.nico.library.repository.AuthorityRepository;
import com.nico.library.repository.UserRepository;
import com.nico.library.security.JwtService;
import com.nico.library.service.AuthenticationService;
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
public class AuthenticationServiceImpl implements AuthenticationService
{
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    //Registrazione utente

    /**
     * Gestisce il processo di registrazione di un nuovo utente nell'applicazione.
     *
     * @param request L'oggetto SignupRequest contenente le informazioni del nuovo utente da registrare.
     * @return Una ResponseEntity contenente un messaggio di conferma o di errore a seconda dell'esito della registrazione.
     */
    public UserSignUpResponse signUp(SignupRequest request)
    {
        // Verifica se l'username o l'email sono già in uso
        if (userRepository.existsByEmailOrUsername(request.getEmail(), request.getUsername()))
        {
            throw new BadRequestException("Username or Email already in use");
        }

        // Recupera l'autorità predefinita per i nuovi utenti
        Authority a = authorityRepository.findByDefaultAuthorityTrue();

        // Costruisce un nuovo oggetto User con le informazioni fornite nella richiesta
        User user = userMapper.signUpAsEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAuthorities(Collections.singleton(a));

        //salva il nuovo utente
        userRepository.save(user);
        return userMapper.signUpAsResponse(user);
    }

    //Accesso utente

    /**
     * Gestisce il processo di accesso dell'utente all'applicazione.
     *
     * @param request L'oggetto SigninRequest contenente le credenziali di accesso dell'utente.
     * @return Una ResponseEntity contenente un oggetto AuthenticationResponse con le informazioni dell'utente e il token JWT generato.
     * @throws org.springframework.security.authentication.BadCredentialsException Se le credenziali fornite non corrispondono a un utente valido.
     */
    public ResponseEntity<?> signIn(SigninRequest request)
    {
        // Cerca l'utente nel repository tramite il nome utente fornito
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        // Confronta la password fornita con quella memorizzata per l'utente
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new BadCredentialsException("Bad credentials");
        }

        // Abilita l'utente se non è già abilitato
        if(!user.isEnabled())
        {
            user.setEnabled(true);
        }

        // Genera un token JWT per l'utente
        String jwtToken = jwtService.generateToken(user, Math.toIntExact(user.getUserId()));

        // Restituisce una ResponseEntity contenente le informazioni dell'utente e il token JWT
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
     * Estrae le stringhe delle autorizzazioni da una collezione di oggetti GrantedAuthority.
     *
     * @param auths Una collezione di oggetti GrantedAuthority che rappresentano le autorizzazioni dell'utente.
     * @return Un array di stringhe di autorizzazioni estratte dalla collezione fornita.
     */
    private String[] authorities(Collection<? extends GrantedAuthority> auths)
    {
        return auths.stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }


}
