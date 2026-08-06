package com.joacocenteno.yoAprendo_api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopicResponse {
    Long id;
    String nombre; 
    String description;
    Integer order;
    Long level_id;
}
