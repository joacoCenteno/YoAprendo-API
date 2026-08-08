package com.joacocenteno.yoAprendo_api.service;

import com.joacocenteno.yoAprendo_api.dto.UserStatisticsResponse;

public interface IStatisticsService {
    public UserStatisticsResponse getUsersStatics(Long user_id);
}
