package com.fitness.tracker.exercise.repository

import com.fitness.tracker.exercise.model.ExerciseRegistration
import com.fitness.tracker.food.model.FoodRegistration
import com.fitness.tracker.person.model.Person
import org.springframework.data.jpa.repository.JpaRepository

import java.time.LocalDate

interface ExerciseRegistrationRepository extends JpaRepository<ExerciseRegistration, Long> {

    List<ExerciseRegistration> findAllExerciseRegistrationByPersonAndRegistrationDate(Person person, LocalDate localDate)

    Optional<ExerciseRegistration> findExerciseRegistrationById(long id)
}