package com.joacocenteno.yoAprendo_api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LessonResponse {
    Long id;
    String title;
    String description;
    Integer order;
    Long topic_id;
}
