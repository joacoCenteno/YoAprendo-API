package com.joacocenteno.yoAprendo_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joacocenteno.yoAprendo_api.model.Lesson;

@Repository
public interface ILessonRepository extends JpaRepository<Lesson, Long>{
    List<Lesson> findByTopicTopicIDOrderByLessosOrder(Long id_topic);
}
