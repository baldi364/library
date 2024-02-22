package com.nico.library.service;

import com.nico.library.entity.Authority;
import com.nico.library.exception.ResourceNotFoundException;
import com.nico.library.repository.AuthorityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityService
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
        return new ResponseEntity("Authority updated", HttpStatus.OK);
    }
}
