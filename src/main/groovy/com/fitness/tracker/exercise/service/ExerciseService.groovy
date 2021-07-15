package com.fitness.tracker.exercise.service

import com.fitness.tracker.exercise.model.Exercise
import com.fitness.tracker.exercise.repository.ExerciseRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic
class ExerciseService {

    @Autowired
    final ExerciseRepository ExerciseRepository

    List<Exercise> findAll(){
        ExerciseRepository.findAll()
    }
}
