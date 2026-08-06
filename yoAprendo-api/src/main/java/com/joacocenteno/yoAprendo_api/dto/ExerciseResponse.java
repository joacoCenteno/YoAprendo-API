package com.joacocenteno.yoAprendo_api.dto;

import com.joacocenteno.yoAprendo_api.model.ExerciseType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExerciseResponse {
    Long id;
    ExerciseType type;
    String json_content;
    Long id_lesson;
}
