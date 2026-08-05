package com.joacocenteno.yoAprendo_api.service;

import com.joacocenteno.yoAprendo_api.dto.ProgressResponse;

public interface IProgressService {
    public ProgressResponse updateProgress(Long user_id, Long lessson_id);
}
