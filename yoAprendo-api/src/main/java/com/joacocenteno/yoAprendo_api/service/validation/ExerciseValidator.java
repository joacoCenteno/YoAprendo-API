package com.joacocenteno.yoAprendo_api.service.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                
            case DRAG:
                return compareDrag(json_content, answer);

            case SORT:
                return compareSort(json_content, answer);

            case LISTEN:
                return compareText(json_content, answer);

            case RELATE:
                return compareRelate(json_content, answer);
        
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

    private Boolean compareDrag(JsonNode content, JsonNode answer){
        
        Integer correct_option = content.get("solution").get("correctOptionId").asInt();

        Integer received_option = content.get("selectedOptionId").asInt();

        return received_option.equals(correct_option);
    }

    private Boolean compareSort(JsonNode content, JsonNode answer){

        List<String> correct_order = new ArrayList<>();
        content.get("solution").get("correctOrder").forEach(node -> correct_order.add(node.asString()));

        List<String> received_order = new ArrayList<>();
        answer.get("order").forEach(node -> received_order.add(node.asString()));

        return correct_order.equals(received_order);
    }

    private Boolean compareRelate(JsonNode content, JsonNode answer){

        Map<Integer, Integer> correct_pairs = new HashMap<>();
        content.get("solution").get("pairs").forEach(pair ->
            correct_pairs.put(pair.get("leftId").asInt(), pair.get("rightId").asInt()));

        Map<Integer, Integer> received_pairs = new HashMap<>();
        answer.get("pairs").forEach(pair ->
            received_pairs.put(pair.get("leftId").asInt(), pair.get("rightId").asInt()));

        return correct_pairs.equals(received_pairs);
    }
}
