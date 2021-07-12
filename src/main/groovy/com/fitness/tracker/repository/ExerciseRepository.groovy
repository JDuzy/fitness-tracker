package com.fitness.tracker.repository

import com.fitness.tracker.model.Exercise
import com.fitness.tracker.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseRepository extends JpaRepository< Exercise, Long> {

    Optional<Exercise> findExerciseById(Long id);

}