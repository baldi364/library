package com.nico.library.service.implementation;

import com.nico.library.dto.mapper.AuthorityMapper;
import com.nico.library.dto.request.authority.AuthorityRequest;
import com.nico.library.dto.response.authority.AuthorityResponse;
import com.nico.library.entity.Authority;
import com.nico.library.exceptions.custom.BadRequestException;
import com.nico.library.exceptions.custom.ResourceNotFoundException;
import com.nico.library.repository.AuthorityRepository;
import com.nico.library.service.AuthorityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService
{
    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;

    public AuthorityResponse addAuthority(AuthorityRequest request) {
        if(authorityRepository.existsByAuthorityName(request.getAuthorityName()))
            throw new BadRequestException("Authority already present");

        Authority authority = authorityMapper.asEntity(request);
        Authority savedAuthority = authorityRepository.save(authority);

        return authorityMapper.asResponse(savedAuthority);
    }

    @Transactional
    public void switchVisibility(byte id)
    {
        Authority a = authorityRepository.findById((int) id)
                .orElseThrow(()-> new ResourceNotFoundException("Authority", "id", id));

        a.setVisible(!a.isVisible());
    }
}
