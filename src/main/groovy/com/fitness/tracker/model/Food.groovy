package com.fitness.tracker.model

import groovy.transform.CompileStatic

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull


@Entity
@Table(name = "food")
@CompileStatic
class Food {

    @Id
    @SequenceGenerator(name = 'food_sequence', sequenceName = 'food_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_sequence")
    @Column( name = "id", updatable = false, nullable = false)
    long id

    @NotNull
    String name

    @NotNull
    BigInteger calories

    @NotNull
    BigDecimal fats

    @NotNull
    BigDecimal carbohydrates

    @NotNull
    BigDecimal proteins

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    Characteristics characteristics


}
