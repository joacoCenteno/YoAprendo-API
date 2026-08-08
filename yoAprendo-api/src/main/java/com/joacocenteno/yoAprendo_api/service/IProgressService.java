package com.joacocenteno.yoAprendo_api.service;

import java.util.List;

import com.joacocenteno.yoAprendo_api.dto.ProgressResponse;

public interface IProgressService {
    public ProgressResponse updateProgress(Long user_id, Long lesson_id);

    public ProgressResponse findProgressByUserAndProgress(Long user_id, Long lesson_id);

    public List<ProgressResponse> findAllProgressByUser(Long user_id);
}
