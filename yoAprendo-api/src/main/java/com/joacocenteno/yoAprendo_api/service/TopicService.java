package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.dto.LessonResponse;
import com.joacocenteno.yoAprendo_api.dto.TopicRequest;
import com.joacocenteno.yoAprendo_api.dto.TopicResponse;
import com.joacocenteno.yoAprendo_api.exception.BadRequestException;
import com.joacocenteno.yoAprendo_api.exception.DuplicateResourceException;
import com.joacocenteno.yoAprendo_api.exception.ResourceNotFoundException;
import com.joacocenteno.yoAprendo_api.mapper.Mapper;
import com.joacocenteno.yoAprendo_api.model.Lesson;
import com.joacocenteno.yoAprendo_api.model.Level;
import com.joacocenteno.yoAprendo_api.model.Topic;
import com.joacocenteno.yoAprendo_api.repository.ILessonRepository;
import com.joacocenteno.yoAprendo_api.repository.ILevelRepository;
import com.joacocenteno.yoAprendo_api.repository.ITopicRepository;

@Service
public class TopicService implements ITopicService{
    
    @Autowired
    ITopicRepository topic_repo;

    @Autowired
    ILevelRepository level_repo;

    @Autowired
    ILessonRepository lesson_repo;

    @Override
    public List<TopicResponse> getAllTopic() {
        return topic_repo.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public TopicResponse getTopicById(Long topic_id) {
        if(topic_id == null) throw new BadRequestException("ID Tema no puede ser nulo");

        Topic topic_obtained = topic_repo.findById(topic_id)
                                            .orElseThrow(() -> new ResourceNotFoundException("Tema con ID "+ topic_id + " inexistente"));
        
        return Mapper.toDto(topic_obtained);
    }

    @Override
    public TopicResponse createTopic(TopicRequest topic) {
        if(topic == null) throw new BadRequestException("Por favor, especifique los datos del tema");
        if(topic.getLevel_id() == null) throw new BadRequestException("Por favor, especifique el ID del nivel");

        Level level = level_repo.findById(topic.getLevel_id())
                                .orElseThrow(() -> new ResourceNotFoundException("Nivel con ID "+ topic.getLevel_id() + " inexistente"));
        

        if(topic.getName() != null && topic_repo.existsByLevelLevelIdAndTopicName(level.getLevelId(), topic.getName())){
            throw new DuplicateResourceException("Tema '"+topic.getName()+"' ya existente dentro del Nivel '"+level.getLevelName()+"'");
        }

        Topic topic_created = Topic.builder()
                                    .topicName(topic.getName())
                                    .topicDescription(topic.getDescription())
                                    .topicOrder(topic.getOrder())
                                    .isActive(topic.getIs_active() != null ? topic.getIs_active() : true)
                                    .build();
        
        return Mapper.toDto(topic_repo.save(topic_created));
    }

    @Override
    public TopicResponse editTopic(Long topic_id, TopicRequest topic) {
       if(topic_id == null) throw new BadRequestException("Por favor, especifique la ID");
        if(topic == null) throw new BadRequestException("Por favor, especifique los datos del tema");

        Topic topic_modified = topic_repo.findById(topic_id)
                                            .orElseThrow(() -> new ResourceNotFoundException("Tema con ID "+topic_id+" inexistente"));

        Long level_id_to_check = topic.getLevel_id() != null ? topic.getLevel_id() : topic_modified.getLevel().getLevelId();

        if(topic.getName() != null && !topic.getName().equals(topic_modified.getTopicName())){
            if(topic_repo.existsByLevelLevelIdAndTopicName(level_id_to_check, topic.getName())){
                throw new DuplicateResourceException("Tema '"+topic.getName()+"' ya existente dentro del Nivel");
            }

            topic_modified.setTopicName(topic.getName());
        }

        if(topic.getDescription() != null) topic_modified.setTopicDescription(topic.getDescription());
        if(topic.getOrder() != null) topic_modified.setTopicOrder(topic.getOrder());
        if(topic.getIs_active() != null) topic_modified.setIsActive(topic.getIs_active());

        if(topic.getLevel_id() != null && !topic.getLevel_id().equals(topic_modified.getLevel().getLevelId())){
            Level level = level_repo.findById(topic.getLevel_id())
                                    .orElseThrow(() -> new DuplicateResourceException("Nivel con ID "+ topic.getLevel_id() +" inexistente"));
            
            topic_modified.setLevel(level);
        }

        return Mapper.toDto(topic_repo.save(topic_modified));
    }

    @Override
    public void toggleActiveTopic(Long topic_id) {
        if (topic_id == null) throw new BadRequestException("Por favor, especifique la ID");

        Topic topic_deactivated = topic_repo.findById(topic_id)
                .orElseThrow(() -> new ResourceNotFoundException("Tema con ID " + topic_id + " inexistente"));

        topic_deactivated.setIsActive(!topic_deactivated.getIsActive());

        topic_repo.save(topic_deactivated);
    }

    @Override
    public List<LessonResponse> getLessonsByTopic(Long topic_id) {
        if (topic_id == null) throw new BadRequestException("Por favor, especifique la ID");

        if (!topic_repo.existsById(topic_id)) throw new ResourceNotFoundException("Tema con ID " + topic_id + " inexistente");

        List<Lesson> lessons = lesson_repo.findByTopicTopicIdOrderByLessonOrder(topic_id);

        return lessons.stream().map(Mapper::toDto).toList();
    }

}
