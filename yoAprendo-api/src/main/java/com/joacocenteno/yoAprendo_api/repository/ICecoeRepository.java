package com.joacocenteno.yoAprendo_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joacocenteno.yoAprendo_api.model.Cecoe;

@Repository
public interface ICecoeRepository extends JpaRepository<Cecoe, Long>{

}
