package com.nico.library.service.implementation;

import com.nico.library.dto.mapper.UserMapper;
import com.nico.library.entity.Authority;
import com.nico.library.entity.User;
import com.nico.library.exceptions.custom.BadRequestException;
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

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    /**
     * Attiva l'account dell'utente corrispondente al token JWT fornito.
     *
     * @param jwt Il token JWT utilizzato per identificare l'utente.
     * @throws ResourceNotFoundException Se l'utente corrispondente al token JWT non viene trovato nel sistema.
     */
    @Transactional
    public void activate(String jwt)
    {
        // Estraggo il nome utente dal token JWT
        String username = jwtService.extractUsername(jwt);

        // Cerco l'utente nel repository e attivo il suo account se trovato
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        user.setEnabled(true);
        userRepository.save(user);
    }

    /**
     * Trova il nome utente corrispondente all'ID utente fornito.
     *
     * @param userId L'ID dell'utente di cui si desidera trovare il nome utente.
     * @return Una ResponseEntity che contiene il nome utente dell'utente trovato.
     * @throws ResourceNotFoundException Se l'utente corrispondente all'ID fornito non viene trovato nel sistema.
     */
    public String findUsername(int userId)
    {
        // Cerco l'utente nel repository degli utenti utilizzando l'ID fornito
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User", "userId", userId));

        // Ottengo il nome utente dall'utente trovato e lo restituisco
        return user.getUsername();
    }

    /**
     * Aggiorna le autorizzazioni dell'utente corrispondente all'ID specificato con le nuove autorizzazioni fornite.
     *
     * @param id          L'ID dell'utente di cui si desidera aggiornare le autorizzazioni.
     * @param authorities Il set delle nuove autorizzazioni da assegnare all'utente.
     * @throws ResourceNotFoundException Se l'utente corrispondente all'ID fornito non viene trovato nel sistema.
     */
    @Transactional
    public void updateAuthorities(int id, Set<String> authorities)
    {
        // Verifico l'esistenza dell'utente
        User u = userRepository.findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("User", "id", id)));

        //Trasformo Set<String> in Set<Authority>
        Set<Authority> auths = authorityRepository.findByVisibleTrueAndAuthorityNameIn(authorities);
        if(auths.isEmpty())
        {
            throw new EmptyListException("authorities");
        }

        //Setto il Set<Authority> su user e salvo
        u.setAuthorities(auths);
        userRepository.save(u);
    }

    /**
     * Ottiene i dettagli dell'utente corrente.
     *
     * @param userDetails Le informazioni sull'utente corrente.
     * @return I dettagli dell'utente corrente.
     */
    public UserResponse getMe(UserDetails userDetails)
    {
        if(userDetails == null){
            throw new BadRequestException("No user logged! Please, log in and try again.");
        }

        // Mappo le informazioni sull'utente corrente in un oggetto UserResponse
        return userMapper.asUserDetailsResponse((User)userDetails);
    }
}
