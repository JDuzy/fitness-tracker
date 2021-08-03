package com.fitness.tracker.food.model


import groovy.transform.CompileStatic

import javax.persistence.Embeddable
import javax.persistence.Transient
import javax.validation.constraints.NotNull
import java.math.RoundingMode

@Embeddable
@CompileStatic
class Nutrients {

    @Transient
    final BigDecimal MINIMUM_PERCENTAGE_TO_BE_MAIN_NUTRIENT = 45

    @NotNull
    final BigDecimal gramsOfCarbohydrates

    @NotNull
    final BigDecimal gramsOfProtein

    @NotNull
    final BigDecimal gramsOfFats

    Nutrients(BigDecimal carbs, BigDecimal proteins, BigDecimal fats){
        gramsOfCarbohydrates = carbs
        gramsOfProtein = proteins
        gramsOfFats = fats
    }

    Nutrients() {

    }

    Integer getCalories(){
        BigDecimal calories = (gramsOfCarbohydrates * 4 + gramsOfProtein * 4 + gramsOfFats * 4).setScale(0, RoundingMode.HALF_UP)
        calories.toInteger()
    }

    Nutrients minus(Nutrients other){
        new Nutrients(this.gramsOfCarbohydrates - other.gramsOfCarbohydrates, this.gramsOfProtein - other.gramsOfProtein, this.gramsOfFats - other.gramsOfFats)
    }

    Nutrients plus(Nutrients other){
        new Nutrients(this.gramsOfCarbohydrates + other.gramsOfCarbohydrates, this.gramsOfProtein + other.gramsOfProtein, this.gramsOfFats + other.gramsOfFats)
    }

    Nutrients addNutrientsBasedOn(FoodRegistration foodRegistration) {
        this + foodRegistration.nutrients
    }

    Nutrients deleteNutrientsBasedOn(FoodRegistration foodRegistration) {
        this - foodRegistration.nutrients
    }

    Nutrients updateNutrientsBasedOn(FoodRegistration foodRegistration, BigDecimal newAmount) {
        Nutrients delete =  this - foodRegistration.nutrients
        delete + foodRegistration.calculateNutrientsIfAmountWere(newAmount)
    }

    Boolean hasSimilarNutrientDistributionTo(Nutrients other){
        /*Nutrients have similar distribution if both Nutrients have 1 specific nutrient representing more than 45% of the grams of the total Nutrients
         or if both of them have none*/
        this.mainNutrient() == other.mainNutrient()
    }

    private String mainNutrient(){
        Map<String, BigDecimal> gramsPerNutrient = ["carbs" : this.gramsOfCarbohydrates, "proteins" : this.gramsOfProtein, "fats" : this.gramsOfFats]
        BigDecimal totalGrams = this.gramsOfCarbohydrates + this.gramsOfProtein + this.gramsOfFats
        Map.Entry<String, BigDecimal> thisMainNutrient =  gramsPerNutrient.entrySet().find {eachNutrient -> ((eachNutrient.getValue() * 100)/ totalGrams) >= MINIMUM_PERCENTAGE_TO_BE_MAIN_NUTRIENT}
        thisMainNutrient?.getKey()
    }

    @Override
     String toString() {
        return "Nutrients{" +
                "Carbs=" + gramsOfCarbohydrates +
                ", Proteins=" + gramsOfProtein +
                ", Fats=" + gramsOfFats +
                '}';
    }

}
