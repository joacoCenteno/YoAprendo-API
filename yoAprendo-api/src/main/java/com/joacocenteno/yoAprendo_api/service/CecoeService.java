package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.dto.CecoeRequest;
import com.joacocenteno.yoAprendo_api.dto.CecoeResponse;
import com.joacocenteno.yoAprendo_api.dto.UserResponse;
import com.joacocenteno.yoAprendo_api.exception.BadRequestException;
import com.joacocenteno.yoAprendo_api.exception.DuplicateResourceException;
import com.joacocenteno.yoAprendo_api.exception.ResourceNotFoundException;
import com.joacocenteno.yoAprendo_api.mapper.Mapper;
import com.joacocenteno.yoAprendo_api.model.Cecoe;
import com.joacocenteno.yoAprendo_api.model.User;
import com.joacocenteno.yoAprendo_api.model.UserRol;
import com.joacocenteno.yoAprendo_api.repository.ICecoeRepository;
import com.joacocenteno.yoAprendo_api.repository.IUserRepository;

@Service
public class CecoeService implements ICecoeService{

    @Autowired
    ICecoeRepository cecoe_repo;

    @Autowired
    IUserRepository user_repo;

    @Override
    public List<CecoeResponse> getAllCecoe() {
        return cecoe_repo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public CecoeResponse getCecoeById(Long cecoe_id) {
        if(cecoe_id == null) throw new BadRequestException("ID Cecoe no puede ser nulo");

        Cecoe cecoe_obtained = cecoe_repo.findById(cecoe_id)
                                    .orElseThrow(() -> new ResourceNotFoundException("Cecoe con ID "+ cecoe_id + " inexistente"));

        return Mapper.toDTO(cecoe_obtained);
    }

    @Override
    public CecoeResponse createCecoe(CecoeRequest cecoe) {
        if(cecoe == null) throw new BadRequestException("Por favor, especifique los datos del Cecoe");
        if(cecoe_repo.existsByCecoeName(cecoe.getName())) throw new DuplicateResourceException("Cecoe con nombre '"+ cecoe.getName() + "' ya existente en la plataforma");

        Cecoe cecoe_created = Cecoe.builder()
                                    .cecoeName(cecoe.getName())
                                    .isActive(cecoe.getIs_active() != null ? cecoe.getIs_active() : true)
                                    .build();

        return Mapper.toDTO(cecoe_repo.save(cecoe_created));
    }

    @Override
    public CecoeResponse editCecoe(Long cecoe_id, CecoeRequest cecoe) {
        if(cecoe_id == null) throw new BadRequestException("Por favor, especifique la ID");
        if(cecoe == null) throw new BadRequestException("Por favor, especifique los datos del Cecoe");

        Cecoe cecoe_modified = cecoe_repo.findById(cecoe_id)
                                    .orElseThrow(() -> new ResourceNotFoundException("Cecoe con ID "+ cecoe_id + " inexistente"));

        if(cecoe.getName() != null && !cecoe.getName().equals(cecoe_modified.getCecoeName())){

            if(cecoe_repo.existsByCecoeName(cecoe.getName())){
                throw new DuplicateResourceException("Cecoe con nombre '"+ cecoe.getName() + "' ya existente en la plataforma");
            }

            cecoe_modified.setCecoeName(cecoe.getName());
        }

        if(cecoe.getIs_active() != null) cecoe_modified.setIsActive(cecoe.getIs_active());

        return Mapper.toDTO(cecoe_repo.save(cecoe_modified));
    }

    @Override
    public void toggleActiveCecoe(Long cecoe_id) {
        if(cecoe_id == null) throw new BadRequestException("Por favor, especifique la ID");

        Cecoe cecoe_deactivated = cecoe_repo.findById(cecoe_id)
                                    .orElseThrow(() -> new ResourceNotFoundException("Cecoe con ID "+ cecoe_id + " inexistente"));

        cecoe_deactivated.setIsActive(!cecoe_deactivated.getIsActive());

        cecoe_repo.save(cecoe_deactivated);
    }

    @Override
    public List<UserResponse> getUsersByCecoe(Long cecoeId) {
        if(cecoeId == null) throw new BadRequestException("Por favor, especifique la ID");

        if(!cecoe_repo.existsById(cecoeId)) throw new ResourceNotFoundException("Cecoe con ID "+ cecoeId + " inexistente");

        checkSupervisorCecoeAccess(cecoeId);

        return user_repo.findByCecoeCecoeId(cecoeId).stream().map(Mapper::toDto).toList();
    }

    @Override
    public List<UserResponse> getUsersByCecoeAndRole(Long cecoeId, UserRol role) {
        if(cecoeId == null) throw new BadRequestException("Por favor, especifique la ID");
        if(role == null) throw new BadRequestException("Por favor, especifique el rol");

        if(!cecoe_repo.existsById(cecoeId)) throw new ResourceNotFoundException("Cecoe con ID "+ cecoeId + " inexistente");

        checkSupervisorCecoeAccess(cecoeId);

        return user_repo.findByCecoeCecoeIdAndUserRol(cecoeId, role).stream().map(Mapper::toDto).toList();
    }

    // Un SUPERVISOR solo puede consultar usuarios de su propio Cecoe
    private void checkSupervisorCecoeAccess(Long cecoeId) {
        User currentUser = getCurrentUser();

        if (currentUser.getUserRol() == UserRol.SUPERVISOR) {
            Long myCecoeId = currentUser.getCecoe() != null ? currentUser.getCecoe().getCecoeId() : null;

            if (myCecoeId == null || !cecoeId.equals(myCecoeId)) {
                throw new AccessDeniedException("Un Supervisor solo puede consultar usuarios de su propio Cecoe");
            }
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new BadRequestException("Sesión no autenticada");
        }

        return user_repo.findByUserPlatformName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado inexistente"));
    }

}
