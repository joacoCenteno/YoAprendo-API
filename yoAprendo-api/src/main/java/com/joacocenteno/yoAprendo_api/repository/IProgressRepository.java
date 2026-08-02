package com.joacocenteno.yoAprendo_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joacocenteno.yoAprendo_api.model.Progress;

@Repository
public interface IProgressRepository extends JpaRepository<Progress, Long>{
    Optional<Progress> findByUserUserIdAndLessonLessonId(Long id_user, Long id_lesson);

    List<Progress> findByUserUserId(Long id_user);
}
