package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.dto.UserRequest;
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
public class UserService implements IUserService{

    @Autowired
    IUserRepository user_repo;

    @Autowired
    ICecoeRepository cecoe_repo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = user_repo.findAll();

        User currentUser = getCurrentUser();

        // Un SUPERVISOR solo ve los usuarios de su propio Cecoe
        if (isSupervisor(currentUser)) {
            Long cecoeId = currentUser.getCecoe() != null ? currentUser.getCecoe().getCecoeId() : null;

            users = users.stream()
                    .filter(u -> u.getCecoe() != null && u.getCecoe().getCecoeId().equals(cecoeId))
                    .toList();
        }

        return users.stream().map(Mapper::toDto).toList();
    }

    @Override
    public UserResponse getUserById(Long userId) {
        if(userId == null) throw new BadRequestException("ID Usuario no puede ser nulo");

        User user_obtained = user_repo.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID "+ userId + " inexistente"));

        User currentUser = getCurrentUser();

        // Un SUPERVISOR no puede ver usuarios de otro Cecoe (no se filtra que existan)
        if (isSupervisor(currentUser)) {
            Long cecoeId = currentUser.getCecoe() != null ? currentUser.getCecoe().getCecoeId() : null;

            if (user_obtained.getCecoe() == null || !user_obtained.getCecoe().getCecoeId().equals(cecoeId)) {
                throw new ResourceNotFoundException("Usuario con ID "+ userId + " inexistente");
            }
        }

        return Mapper.toDto(user_obtained);
    }

    @Override
    public UserResponse createUser(UserRequest user) {
        if(user == null) throw new BadRequestException("Por favor, especifique los datos del usuario");
        if(user_repo.findByEmail(user.getEmail()).isPresent()) throw new DuplicateResourceException("Usuario con email '"+ user.getEmail() + "' ya existente en la plataforma");

        User currentUser = getCurrentUser();

        if (isSupervisor(currentUser)) {
            if (user.getRol() != UserRol.STUDENT) {
                throw new BadRequestException("Un Supervisor solamente puede crear estudiantes");
            }

            if (user.getCecoe_id() != null
                    && (currentUser.getCecoe() == null || !user.getCecoe_id().equals(currentUser.getCecoe().getCecoeId()))) {
                throw new BadRequestException("Un Supervisor solo puede crear estudiantes de su propio Cecoe");
            }
        }

        Long cecoeId = user.getCecoe_id() != null
                ? user.getCecoe_id()
                : (isSupervisor(currentUser) && currentUser.getCecoe() != null ? currentUser.getCecoe().getCecoeId() : null);

        Cecoe cecoe = null;
        if (cecoeId != null) {
            cecoe = cecoe_repo.findById(cecoeId)
                                .orElseThrow(() -> new ResourceNotFoundException("Cecoe con ID "+ cecoeId + " inexistente"));
        }

        String user_platform_name_created = createUserPlatformName(user);

        while (existInPlatform(user_platform_name_created)) {

            user_platform_name_created = createUserPlatformName(user);
        }

        User user_created = User.builder()
                                .userName(user.getName())
                                .userSurname(user.getSurname())
                                .email(user.getEmail())
                                .password(passwordEncoder.encode(user.getPassword()))
                                .userPlatformName(user_platform_name_created)
                                .userRol(user.getRol())
                                .userActive(user.getIs_active() != null ? user.getIs_active() : true)
                                .cecoe(cecoe)
                                .build();

        return Mapper.toDto(user_repo.save(user_created));
    }

    @Override
    public UserResponse editUser(Long userId, UserRequest user) {
        if(userId == null) throw new BadRequestException("Por favor, especifique la ID");
        if(user == null) throw new BadRequestException("Por favor, especifique los datos del usuario");

        User user_modified = user_repo.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID "+ userId + " inexistente"));

        if(user.getEmail() != null && !user.getEmail().equals(user_modified.getEmail())){

            if(user_repo.findByEmail(user.getEmail()).isPresent()){
                throw new DuplicateResourceException("Usuario con email '"+ user.getEmail() + "' ya existente en la plataforma");
            }

            user_modified.setEmail(user.getEmail());
        }

        if(user.getName() != null) user_modified.setUserName(user.getName());
        if(user.getSurname() != null) user_modified.setUserSurname(user.getSurname());
        if(user.getPassword() != null) user_modified.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRol() != null) user_modified.setUserRol(user.getRol());
        if(user.getIs_active() != null) user_modified.setUserActive(user.getIs_active());

        if(user.getCecoe_id() != null && (user_modified.getCecoe() == null || !user.getCecoe_id().equals(user_modified.getCecoe().getCecoeId()))){
            Cecoe cecoe = cecoe_repo.findById(user.getCecoe_id())
                                .orElseThrow(() -> new ResourceNotFoundException("Cecoe con ID "+ user.getCecoe_id() + " inexistente"));

            user_modified.setCecoe(cecoe);
        }

        return Mapper.toDto(user_repo.save(user_modified));
    }

    @Override
    public void toggleActiveUser(Long userId) {
        if(userId == null) throw new BadRequestException("Por favor, especifique la ID");

        User user_deactivated = user_repo.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID "+ userId + " inexistente"));

        user_deactivated.setUserActive(!user_deactivated.getUserActive());

        user_repo.save(user_deactivated);
    }

    private String createUserPlatformName(UserRequest user){
        Integer random_number = (int) (Math.random() * 999) + 1;

        return user.getName().toLowerCase() + user.getSurname().toLowerCase() + random_number;
    }

    private boolean existInPlatform(String userNameCreated){
        return user_repo.existsByUserPlatformName(userNameCreated);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new BadRequestException("Sesión no autenticada");
        }

        return user_repo.findByUserPlatformName(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado inexistente"));
    }

    private boolean isSupervisor(User currentUser) {
        return currentUser.getUserRol() == UserRol.SUPERVISOR;
    }

}
