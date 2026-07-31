package com.joacocenteno.yoAprendo_api.dto;

import com.joacocenteno.yoAprendo_api.model.PorgressStatus;

import lombok.Builder;

@Builder
public class ProgressResponse {
    Long id;
    Long user_id;
    Long lesson_id;
    PorgressStatus status;
    Integer percent;
}
