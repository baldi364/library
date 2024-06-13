package com.nico.library.service.implementation;

import com.nico.library.entity.Authority;
import com.nico.library.exceptions.custom.ResourceNotFoundException;
import com.nico.library.repository.AuthorityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl
{
    private final AuthorityRepository authorityRepository;

    public ResponseEntity<?> addAuthority(String newAuthority) {
        if(authorityRepository.existsByAuthorityName(newAuthority))
            return new ResponseEntity<>("Authority already present", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(authorityRepository.save(new Authority(newAuthority)), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> switchVisibility(int id)
    {
        Authority a = authorityRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Authority", "id", id));

        a.setVisible(!a.isVisible());
        return new ResponseEntity<>("Authority updated", HttpStatus.OK);
    }
}
