package com.fitness.tracker.model

import com.fitness.tracker.model.registration.FoodRegistration
import groovy.transform.CompileStatic

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull
import java.time.LocalDate

@Entity
@Table(name = "daily_nutrients_eaten")
@CompileStatic
class DailyNutrientsEaten {

    @Id
    @SequenceGenerator(name = 'daily_nutrients_eaten_sequence', sequenceName = 'daily_nutrients_eaten_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "daily_nutrients_eaten_sequence")
    @Column( name = "id", updatable = false, nullable = false)
    Long id

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutrients_id", referencedColumnName = "id")
    Nutrients nutrients

    @NotNull
    LocalDate eatenDay

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    Person person

    void addNutrientsBasedOn(FoodRegistration foodRegistration) {
        nutrients.addNutrientsBasedOn(foodRegistration)
    }

    void deleteNutrientsBasedOn(FoodRegistration foodRegistration) {
        nutrients.deleteNutrientsBasedOn(foodRegistration)
    }

    Integer getCalories(){
        nutrients.calories
    }
}
