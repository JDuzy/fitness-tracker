package com.fitness.tracker.exercise.model

import com.fitness.tracker.person.model.Registration

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "exercise_registration")
class ExerciseRegistration extends Registration{

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    Exercise exercise

    @NotNull
    BigDecimal time //create class duration with HH:MM:SS or import from somewhere

    @NotNull
    BigDecimal weight

}