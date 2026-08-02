package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import com.joacocenteno.yoAprendo_api.dto.LessonResponse;
import com.joacocenteno.yoAprendo_api.dto.TopicRequest;
import com.joacocenteno.yoAprendo_api.dto.TopicResponse;

public interface ITopicService {
    public List<TopicResponse> getAllTopic();
    public TopicResponse getTopicById(Long topic_id);
    public TopicResponse createTopic(TopicRequest topic);
    public TopicResponse editTopic(Long topic_id, TopicRequest topic);
    public void deactivateTopic(Long id);
    public List<LessonResponse> getLessonsByTopic(Long topic_id);
}
