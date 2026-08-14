package com.joacocenteno.yoAprendo_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class TokenResponse {
    private String access_token;
}
