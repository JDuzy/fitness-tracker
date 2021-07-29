package com.fitness.tracker.food.model

import groovy.transform.CompileStatic

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "daily_nutritional_objective")
@CompileStatic
class DailyNutritionalObjective {

    @Id
    @SequenceGenerator(name = 'nutritional_objective_sequence', sequenceName = 'nutritional_objective_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nutritional_objective_sequence")
    @Column( name = "person_id", updatable = false, nullable = false)
    Long id

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutrients_id", referencedColumnName = "id")
    Nutrients objectiveNutrients = new Nutrients()


    void calculateObjective(Integer age, BigDecimal weight, Integer height, String sex, BigDecimal physicalActivity, BigDecimal weightChangePerWeek){
        BigDecimal brm
        if (sex == "Male"){
            brm =  66 + (13.7 * weight) + (5 * height) - (6.8 * age)   //Harris-Benedict Equation
        }
        else{
            brm =  655 + (9.6 * weight) + (1.8 * height) - (4.7 * age) // Harris-Benedict Equation
        }

        BigDecimal objectiveCalories = (brm * physicalActivity) + weightChangePerWeek    //Katch-McArdle multipliers

        distributePersonNutrients(objectiveCalories, weight)
    }


    Nutrients calculateRemainingNutrients(DailyNutrientsEaten dailyNutrientsEaten) {
        this.objectiveNutrients - dailyNutrientsEaten.nutrients
    }

    private void distributePersonNutrients(BigDecimal objectiveCalories, BigDecimal weight){
        BigDecimal proteins = weight * 2
        BigDecimal fats = weight
        BigDecimal caloriesFromProteinAndFats = proteins * 4 + fats * 9
        BigDecimal caloriesForCarbohydrates = objectiveCalories - caloriesFromProteinAndFats
        BigDecimal carbohydrates = caloriesForCarbohydrates / 4

        objectiveNutrients.update(new Nutrients(gramsOfCarbohydrates: carbohydrates, gramsOfProtein: proteins, gramsOfFats: fats))
    }


    Integer calculateRemainingCalories(DailyNutrientsEaten dailyNutrientsEaten) {
        calculateRemainingNutrients(dailyNutrientsEaten).calories
    }

    Integer getObjectiveCalories() {
        objectiveNutrients.calories
    }
}
