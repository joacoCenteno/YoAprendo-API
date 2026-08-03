package com.joacocenteno.yoAprendo_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joacocenteno.yoAprendo_api.model.Topic;

@Repository
public interface ITopicRepository extends JpaRepository<Topic, Long>{
    List<Topic> findByLevelLevelIdOrderByTopicOrder(Long id_level);

    Boolean existsByLevelLevelIdAndTopicName(Long level_id, String topic_name);
}
