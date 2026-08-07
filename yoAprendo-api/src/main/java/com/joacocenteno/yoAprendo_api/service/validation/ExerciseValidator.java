package com.joacocenteno.yoAprendo_api.service.validation;

import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.model.Exercise;
import com.joacocenteno.yoAprendo_api.model.ExerciseType;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class ExerciseValidator {

    private final ObjectMapper mapper = new ObjectMapper();

    public Boolean validate(Exercise exercise, JsonNode answer){

        JsonNode json_content = mapper.readTree(exercise.getJsonContent());

        ExerciseType exercise_type = exercise.getExerciseType();

        switch (exercise_type) {
            case WRITE:
                return compareText(json_content, answer);            
        
            default: 
                return false;
        }
    }


    private Boolean compareText(JsonNode content, JsonNode answer){

        JsonNode accepted_answers = content.get("solution").get("acceptedAnswers");

        String received = answer.get("text").asString().trim().toLowerCase();

        for (JsonNode candidate : accepted_answers) {
            if (candidate.asString().trim().toLowerCase().equals(received)) {
                return true;
            }
        }

        return false;
    }
}
