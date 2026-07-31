package com.joacocenteno.yoAprendo_api.dto;

import lombok.Builder;

@Builder
public class CecoeResponse {
    Long id;
    String name;
    Boolean is_active;
}
