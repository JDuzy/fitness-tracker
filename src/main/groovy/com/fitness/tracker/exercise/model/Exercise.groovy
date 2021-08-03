package com.fitness.tracker.exercise.model

import groovy.transform.CompileStatic

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull


@Entity
@Table(name = "exercise")
@CompileStatic
class Exercise {

    enum Type {AEROBIC, STRENGTH}

    @Id
    @SequenceGenerator(name = 'exercise_sequence', sequenceName = 'exercise_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_sequence")
    @Column( name = "id", updatable = false, nullable = false)
    long id

    @NotNull
    String name

    @NotNull
    BigDecimal caloriesBurnedPerMinute

    @NotNull
    Type type

    Boolean isAerobic() {
        type == Type.AEROBIC
    }

    Boolean isStrength() {
        type == Type.STRENGTH
    }

    BigDecimal getTotalCaloriesBurned(BigDecimal time) {
        caloriesBurnedPerMinute * time
    }



}

