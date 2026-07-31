package com.joacocenteno.yoAprendo_api.dto;

import com.joacocenteno.yoAprendo_api.model.ExerciseType;

import lombok.Builder;

@Builder
public class ExerciseResponse {
    Long id;
    ExerciseType type;
    String json_content;
}
