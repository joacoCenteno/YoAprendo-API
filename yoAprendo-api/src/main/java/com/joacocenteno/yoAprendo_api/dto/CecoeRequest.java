package com.joacocenteno.yoAprendo_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CecoeRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    String name;
}
