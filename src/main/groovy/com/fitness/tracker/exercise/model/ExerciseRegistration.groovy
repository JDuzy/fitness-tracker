package com.fitness.tracker.exercise.model

import com.fitness.tracker.person.model.Registration

import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "exercise_registration")
class ExerciseRegistration extends Registration{

    @NotNull
    Exercise exercise

    @NotNull
    BigDecimal time //create class duration with HH:MM:SS or import from somewhere

}