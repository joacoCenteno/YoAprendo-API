package com.joacocenteno.yoAprendo_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.JsonNode;

@Getter @Setter
public class ExerciseProgressRequest {

    @NotNull 
    private Long user_id;

    @NotNull
    private Long exercise_id;

    @NotNull
    private JsonNode answer;
}
