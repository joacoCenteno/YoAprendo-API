package com.joacocenteno.yoAprendo_api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LevelResponse {
    Long id;
    String name;
    String description;
    Integer order;
    Long course_id;
}
