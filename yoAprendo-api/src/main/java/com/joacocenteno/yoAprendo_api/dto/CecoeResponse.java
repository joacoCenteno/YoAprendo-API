package com.joacocenteno.yoAprendo_api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CecoeResponse {
    Long id;
    String name;
    Boolean is_active;
}
