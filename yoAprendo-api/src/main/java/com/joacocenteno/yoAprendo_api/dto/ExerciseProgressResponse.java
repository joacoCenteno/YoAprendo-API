package com.joacocenteno.yoAprendo_api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExerciseProgressResponse {
    Long id;
    Long user_id;
    Long exercise_id;
    Integer attempts;
    Boolean is_completed;
}
