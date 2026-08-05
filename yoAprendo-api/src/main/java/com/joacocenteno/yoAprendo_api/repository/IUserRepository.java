package com.joacocenteno.yoAprendo_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joacocenteno.yoAprendo_api.model.User;
import com.joacocenteno.yoAprendo_api.model.UserRol;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);

    List<User> findByCecoeCecoeId(Long cecoe_id);

    List<User> findByCecoeCecoeIdAndUserRol(Long cecoe_id, UserRol rol);
}
