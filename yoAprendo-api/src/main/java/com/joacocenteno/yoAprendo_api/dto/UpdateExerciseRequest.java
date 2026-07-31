package com.joacocenteno.yoAprendo_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateExerciseRequest {
    @NotBlank(message = "El contenido no puede estar vacío")
    String json_content;
}
