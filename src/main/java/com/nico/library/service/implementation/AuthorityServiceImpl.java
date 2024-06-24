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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService
{
    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;

    /**
     * Adds a new authority to the system based on the provided request.
     *
     * @param request AuthorityRequest object containing the name of the authority to add.
     * @return AuthorityResponse object containing the name of the added authority.
     * @throws BadRequestException if the authority name is invalid or already exists.
     */
    public AuthorityResponse addAuthority(AuthorityRequest request) {

        String authorityName = request.getAuthorityName().toUpperCase();

        // Check if role is valid
        if(!isValidAuthority(authorityName)){
            throw new BadRequestException(String.format("Authority '%s' is invalid, it must starts with 'ROLE_'", authorityName));
        }

        if(authorityRepository.existsByAuthorityName(authorityName))
            throw new BadRequestException(String.format("Authority '%s' already present", authorityName));

        Authority authority = authorityMapper.asEntity(request);
        Authority savedAuthority = authorityRepository.save(authority);

        return authorityMapper.asResponse(savedAuthority);
    }

    /**
     * Toggles the visibility of the authority identified by the provided ID.
     *
     * @param id The ID of the authority whose visibility is to be toggled.
     * @throws ResourceNotFoundException if no authority is found with the provided ID.
     */
    @Transactional
    public void switchVisibility(byte id)
    {
        Authority a = authorityRepository.findById((int) id)
                .orElseThrow(()-> new ResourceNotFoundException("Authority", "id", id));

        a.setVisible(!a.isVisible());
    }

    /**
     * Checks if the provided authority name is valid.
     * An authority name is considered valid if it is not null, not blank, and starts with 'ROLE_'.
     *
     * @param authorityName The authority name to validate.
     * @return true if the authority name is valid, false otherwise.
     * @throws BadRequestException if the authority name is null or blank.
     */
    public boolean isValidAuthority(String authorityName){
        if(authorityName == null || authorityName.isBlank()){
            throw new BadRequestException("Please, enter an authority!");
        }
        return authorityName.startsWith("ROLE_");
    }
}
