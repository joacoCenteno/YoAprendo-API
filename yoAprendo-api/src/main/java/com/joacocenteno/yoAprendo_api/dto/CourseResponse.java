package com.joacocenteno.yoAprendo_api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseResponse {

    Long id;
    String name;
    String description;
}
