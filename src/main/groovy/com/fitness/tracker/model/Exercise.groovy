package com.fitness.tracker.model

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

    @Id
    @Column( name = "id", updatable = false, nullable = false)
    long id

    @NotNull
    String name

    @NotNull
    enum type

    boolean isCardio() {
    }

}

