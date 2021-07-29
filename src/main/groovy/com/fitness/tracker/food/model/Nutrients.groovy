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
import java.util.stream.Collectors

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
    BigDecimal gramsOfCarbohydrates

    @NotNull
    BigDecimal gramsOfProtein

    @NotNull
    BigDecimal gramsOfFats


    Integer getCalories(){
        BigDecimal calories = (gramsOfCarbohydrates * 4 + gramsOfProtein * 4 + gramsOfFats * 4).setScale(0, RoundingMode.HALF_UP)
        calories.toInteger()
    }

    Nutrients minus(Nutrients other){
        new Nutrients(gramsOfCarbohydrates: this.gramsOfCarbohydrates - other.gramsOfCarbohydrates, gramsOfProtein: this.gramsOfProtein - other.gramsOfProtein, gramsOfFats: this.gramsOfFats - other.gramsOfFats)
    }

    Nutrients plus(Nutrients other){
        new Nutrients(gramsOfCarbohydrates: this.gramsOfCarbohydrates + other.gramsOfCarbohydrates, gramsOfProtein: this.gramsOfProtein + other.gramsOfProtein, gramsOfFats: this.gramsOfFats + other.gramsOfFats)
    }

    void update(Nutrients other) {
        this.gramsOfCarbohydrates = other.gramsOfCarbohydrates
        this.gramsOfProtein = other.gramsOfProtein
        this.gramsOfFats = other.gramsOfFats
    }

    void addNutrientsBasedOn(FoodRegistration foodRegistration) {
        update(this + foodRegistration.nutrients)
    }

    void deleteNutrientsBasedOn(FoodRegistration foodRegistration) {
        update(this - foodRegistration.nutrients)
    }

    void updateNutrientsBasedOn(FoodRegistration foodRegistration, BigDecimal newAmount) {
        update(this - foodRegistration.nutrients)
        update(this + foodRegistration.calculateNutrientsIfAmountWere(newAmount))
    }

    Boolean hasSameMainNutrient(Nutrients other){
        Map<String, BigDecimal> thisGramsPerNutrient = ["carbs" : this.gramsOfCarbohydrates, "proteins" : this.gramsOfProtein, "fats" : this.gramsOfFats]
        BigDecimal thisTotalGrams = thisGramsPerNutrient.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add)
        Map.Entry<String, BigDecimal> thisMainNutrient =  thisGramsPerNutrient.entrySet().find {nutrientWithItsPercentage-> ((nutrientWithItsPercentage.getValue() * 100)/ thisTotalGrams) > 50}

        Map<String, BigDecimal> otherGramsPerNutrient = ["carbs" : other.gramsOfCarbohydrates, "proteins" : other.gramsOfProtein, "fats" : other.gramsOfFats]
        BigDecimal otherTotalGrams = thisGramsPerNutrient.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add)

        Map.Entry<String, BigDecimal> otherMainNutrient =  thisGramsPerNutrient.entrySet().find {nutrientWithItsPercentage-> ((nutrientWithItsPercentage.getValue() * 100)/ otherTotalGrams) > 50}


    }

    void func(){
        List<Integer> lists = new ArrayList<Integer>()
    }
}
