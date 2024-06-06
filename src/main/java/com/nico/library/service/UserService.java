package com.nico.library.service;

import com.nico.library.entity.Authority;
import com.nico.library.entity.User;
import com.nico.library.exceptions.custom.ResourceNotFoundException;
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

    /**
     * Attiva l'account dell'utente corrispondente al token JWT fornito.
     *
     * @param jwt Il token JWT utilizzato per identificare l'utente.
     * @return Una ResponseEntity che conferma l'attivazione dell'account dell'utente.
     * @throws ResourceNotFoundException Se l'utente corrispondente al token JWT non viene trovato nel sistema.
     */
    @Transactional
    public ResponseEntity<?> activate(String jwt)
    {
        // Estraggo il nome utente dal token JWT
        String username = jwtService.extractUsername(jwt);

        // Cerco l'utente nel repository e attivo il suo account se trovato
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        user.setEnabled(true);

        // Restituisco una ResponseEntity che conferma l'attivazione dell'account dell'utente
        return new ResponseEntity<>("Welcome " + username + ", your account is activated", HttpStatus.OK);
    }

    /**
     * Trova il nome utente corrispondente all'ID utente fornito.
     *
     * @param userId L'ID dell'utente di cui si desidera trovare il nome utente.
     * @return Una ResponseEntity che contiene il nome utente dell'utente trovato.
     * @throws ResourceNotFoundException Se l'utente corrispondente all'ID fornito non viene trovato nel sistema.
     */
    public ResponseEntity<?> findUsername(int userId)
    {
        // Cerco l'utente nel repository degli utenti utilizzando l'ID fornito
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User", "userId", userId));

        // Ottengo il nome utente dall'utente trovato e lo restituisco come parte della ResponseEntity
        String username = user.getUsername();
        return new ResponseEntity<>("Username: " + username, HttpStatus.OK);
    }

    /**
     * Aggiorna le autorizzazioni dell'utente corrispondente all'ID specificato con le nuove autorizzazioni fornite.
     *
     * @param id          L'ID dell'utente di cui si desidera aggiornare le autorizzazioni.
     * @param authorities Il set delle nuove autorizzazioni da assegnare all'utente.
     * @return Una ResponseEntity che conferma il successo dell'aggiornamento delle autorizzazioni per l'utente specificato.
     * @throws ResourceNotFoundException Se l'utente corrispondente all'ID fornito non viene trovato nel sistema.
     */
    public ResponseEntity<?> updateAuthorities(int id, Set<String> authorities)
    {
        // Verifico l'esistenza dell'utente
        User u = userRepository.findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("User", "id", id)));

        //Trasformo Set<String> in Set<Authority>
        Set<Authority> auths = authorityRepository.findByVisibleTrueAndAuthorityNameIn(authorities);
        if(auths.isEmpty())
        {
            return new ResponseEntity<>("Authorities not found", HttpStatus.NOT_FOUND);
        }

        //Setto il Set<Authority> su user e salvo
        u.setAuthorities(auths);
        userRepository.save(u);
        return new ResponseEntity<>("Authorities updated for user " + u.getUsername(), HttpStatus.OK);
    }

    /**
     * Ottiene i dettagli dell'utente corrente.
     *
     * @param userDetails Le informazioni sull'utente corrente.
     * @return Una ResponseEntity contenente i dettagli dell'utente corrente.
     */
    public ResponseEntity<?> getMe(UserDetails userDetails)
    {
        // Converto le informazioni sull'utente corrente in un oggetto UserResponse
        UserResponse u = UserResponse.fromUserDetailsToUserResponse((User) userDetails);
        return new ResponseEntity<>(u,HttpStatus.OK);
    }
}
