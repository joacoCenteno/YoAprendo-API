package com.joacocenteno.yoAprendo_api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserStatisticsResponse {
    Long user_id;

    Integer lessons_total;
    Integer lessons_completed;
    Integer lessons_in_progress;
    Integer lessons_not_started;

    Integer exercises_total;
    Integer exercises_completed;

    Integer total_attempts;

    Double progress_percent;
}
