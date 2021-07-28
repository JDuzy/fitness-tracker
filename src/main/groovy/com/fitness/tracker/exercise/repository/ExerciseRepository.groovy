package com.fitness.tracker.exercise.repository

import com.fitness.tracker.exercise.model.Exercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseRepository extends JpaRepository< Exercise, Long> {

    Optional<Exercise> findExerciseById(Long id);

}