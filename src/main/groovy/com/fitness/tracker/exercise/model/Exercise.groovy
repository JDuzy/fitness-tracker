package com.fitness.tracker.exercise.model

import groovy.transform.CompileStatic

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull


@Entity
@Table(name = "exercise")
@CompileStatic
class Exercise {

    enum Type {AEROBIC, STRENGTH}

    @Id
    @Column( name = "id", updatable = false, nullable = false)
    long id

    @NotNull
    String name

    @NotNull
    BigDecimal caloriesBurned

    @NotNull
    Type type

    boolean isAerobic() {
        if (type == Type.AEROBIC)
            true
    }

    boolean isStrength() {
        if (type == Type.STRENGTH)
            true
    }

}

