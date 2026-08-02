package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import com.joacocenteno.yoAprendo_api.dto.LevelRequest;
import com.joacocenteno.yoAprendo_api.dto.LevelResponse;
import com.joacocenteno.yoAprendo_api.dto.TopicResponse;

public interface ILevelService {
    public List<LevelResponse> getAllLevel();
    public LevelResponse getLevelById(Long level_id);
    public LevelResponse createLevel(LevelRequest level);
    public LevelResponse editLevel(Long id, LevelRequest level);
    public void deactivateLevel(Long id);
    public List<TopicResponse> getTopicsByLevel(Long level_id);
}
