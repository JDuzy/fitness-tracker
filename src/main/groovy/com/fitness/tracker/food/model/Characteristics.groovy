package com.fitness.tracker.food.model

import groovy.transform.CompileStatic

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "characteristics")
@CompileStatic
class Characteristics {

    @Id
    @Column( name = "food_id", updatable = false, nullable = false)
    long id

    @NotNull
    Boolean isVegetarian

    @NotNull
    Boolean isVegan

    @NotNull
    Boolean isPescetarian

}
