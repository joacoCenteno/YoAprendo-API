package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return user_repo.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public UserResponse getUserById(Long userId) {
        if(userId == null) throw new BadRequestException("ID Usuario no puede ser nulo");

        User user_obtained = user_repo.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID "+ userId + " inexistente"));

        return Mapper.toDto(user_obtained);
    }

    @Override
    public UserResponse createUser(UserRequest user) {
        if(user == null) throw new BadRequestException("Por favor, especifique los datos del usuario");
        if(user_repo.findByEmail(user.getEmail()).isPresent()) throw new DuplicateResourceException("Usuario con email '"+ user.getEmail() + "' ya existente en la plataforma");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentUserRole = authentication.getAuthorities()
                                                .stream()
                                                .findFirst()
                                                .map(GrantedAuthority::getAuthority)
                                                .orElse("");
        
        if(currentUserRole.equals("ROLE_SUPERVISOR") && user.getRol() != UserRol.STUDENT){
            throw new BadRequestException("Un Supervisor solamente puede crear estudiantes");
        }                                      

        Cecoe cecoe = null;
        if(user.getCecoe_id() != null){
            cecoe = cecoe_repo.findById(user.getCecoe_id())
                                .orElseThrow(() -> new ResourceNotFoundException("Cecoe con ID "+ user.getCecoe_id() + " inexistente"));
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
        return user_repo.existByUserPlatformName(userNameCreated);
    }

}
