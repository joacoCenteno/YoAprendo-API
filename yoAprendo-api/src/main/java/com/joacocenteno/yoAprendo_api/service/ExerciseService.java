package com.joacocenteno.yoAprendo_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joacocenteno.yoAprendo_api.dto.ExerciseResponse;
import com.joacocenteno.yoAprendo_api.dto.UpdateExerciseRequest;
import com.joacocenteno.yoAprendo_api.exception.BadRequestException;
import com.joacocenteno.yoAprendo_api.exception.ResourceNotFoundException;
import com.joacocenteno.yoAprendo_api.mapper.Mapper;
import com.joacocenteno.yoAprendo_api.model.Exercise;
import com.joacocenteno.yoAprendo_api.repository.IExerciseRepository;

@Service
public class ExerciseService implements IExerciseService{
    @Autowired
    IExerciseRepository exercise_repo;

    @Override
    public ExerciseResponse editExercise(Long id, UpdateExerciseRequest exercise) {
        if(exercise == null) throw new BadRequestException("Por favor, especifique los datos del ejercicio");
        if(id == null) throw new BadRequestException("Por favor, especifique la ID del ejercicio");

        Exercise exercise_modified = exercise_repo.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Ejercicio con ID "+ id + " inexistente"));

        if (exercise.getJson_content() == null || exercise.getJson_content().isBlank()) throw new BadRequestException("El contenido JSON no puede estar vacío");

        if (exercise.getJson_content().equals(exercise_modified.getJsonContent())) throw new BadRequestException("El contenido JSON es igual al existente");


        exercise_modified.setJsonContent(exercise.getJson_content());

        return Mapper.toDto(exercise_repo.save(exercise_modified));
    }

    @Override
    public ExerciseResponse getExerciseById(Long exercise_id) {
        if(exercise_id == null) throw new BadRequestException("ID Tema no puede ser nulo");

        Exercise exercise = exercise_repo.findById(exercise_id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Ejercicio con ID "+exercise_id+" inexistente"));

        return Mapper.toDto(exercise);
    }
}
