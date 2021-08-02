package com.fitness.tracker.food.model


import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.springframework.data.annotation.Transient

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

    @Transient
    BigDecimal MINIMUM_PERCENTAGE_TO_BE_MAIN_NUTRIENT = 45


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
