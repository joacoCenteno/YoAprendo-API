package com.joacocenteno.yoAprendo_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joacocenteno.yoAprendo_api.dto.UserStatisticsResponse;
import com.joacocenteno.yoAprendo_api.service.IStatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/estadisticas")
public class StatisticsController{

    @Autowired
    IStatisticsService statistics_serv;

    @GetMapping("/user/{user_id}")
    public ResponseEntity<UserStatisticsResponse> getUserStatisticsController(@RequestParam Long user_id) {
        return ResponseEntity.status(HttpStatus.OK).body(statistics_serv.getUsersStatics(user_id));
    }
    
}
