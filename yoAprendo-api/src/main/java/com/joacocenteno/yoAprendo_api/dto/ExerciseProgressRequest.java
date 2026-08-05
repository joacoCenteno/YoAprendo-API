package com.joacocenteno.yoAprendo_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExerciseProgressRequest {

    @NotNull 
    Long user_id;

    @NotNull
    Long exercise_id;

    @NotNull
    Boolean is_correct;
}
