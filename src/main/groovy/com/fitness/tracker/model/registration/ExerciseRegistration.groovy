package com.fitness.tracker.model.registration

import com.fitness.tracker.model.Exercise;
import com.fitness.tracker.model.Food

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.Table
import javax.validation.constraints.NotNull
import java.math.BigDecimal;

@Entity
@Table(name = "exercise_registration")
class ExerciseRegistration extends Registration{

    @NotNull
    Exercise exercise

    @NotNull
    BigDecimal time


}