package com.fitness.tracker.food.model


import groovy.transform.CompileStatic

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull
import java.math.RoundingMode

@Entity
@Table(name = "nutrients")
@CompileStatic
class Nutrients {

    @Id
    @SequenceGenerator(name = 'nutrient_sequence', sequenceName = 'nutrient_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nutrient_sequence")
    @Column( name = "id", updatable = false, nullable = false)
    long id

    @NotNull
    BigDecimal carbohydrates

    @NotNull
    BigDecimal proteins

    @NotNull
    BigDecimal fats


    Integer getCalories(){
        BigDecimal calories = (carbohydrates * 4 + proteins * 4 + fats * 4).setScale(0, RoundingMode.HALF_UP)
        calories.toInteger()
    }

    Nutrients minus(Nutrients other){
        new Nutrients(carbohydrates: this.carbohydrates - other.carbohydrates, proteins: this.proteins - other.proteins, fats: this.fats - other.fats)
    }

    Nutrients plus(Nutrients other){
        new Nutrients(carbohydrates: this.carbohydrates + other.carbohydrates, proteins: this.proteins + other.proteins, fats: this.fats + other.fats)
    }

    void update(Nutrients other) {
        this.carbohydrates = other.carbohydrates
        this.proteins = other.proteins
        this.fats = other.fats
    }

    void addNutrientsBasedOn(FoodRegistration foodRegistration) {
        update(this + foodRegistration.nutrients)
    }

    void deleteNutrientsBasedOn(FoodRegistration foodRegistration) {
        update(this - foodRegistration.nutrients)
    }

    /*void updateNutrientsBasedOn(FoodRegistration foodRegistration, BigDecimal newAmount) {

    }*/
}
