package com.fitness.tracker.person.model

import com.fitness.tracker.exercise.model.Exercise
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import javax.validation.constraints.NotNull


@EqualsAndHashCode
@CompileStatic
class PhysicalObjective {

    @NotNull
    BigDecimal addedCaloriesFromObjective

    PhysicalObjective(BigDecimal addedCaloriesFromObjective){
        this.addedCaloriesFromObjective = validate(addedCaloriesFromObjective)
    }

    PhysicalObjective(){

    }

    static BigDecimal validate(BigDecimal addedCaloriesFromObjective){
        if(!isValid(addedCaloriesFromObjective)){
            throw new IllegalArgumentException("Added calories must be between -300 and 300")
        }
        return addedCaloriesFromObjective
    }

    static Boolean isValid(BigDecimal addedCaloriesFromObjective){
        if (addedCaloriesFromObjective >= -300 && addedCaloriesFromObjective <= 300){
            return true
        }
        return false
    }

    Boolean isExerciseOptimum(Exercise exercise){
        if (addedCaloriesFromObjective >= 0){
            return exercise.isStrength()
        }
        else if (addedCaloriesFromObjective <= 0){
            return exercise.isAerobic()
        }
    }

    BigDecimal addObjectiveCalories(BigDecimal maintenanceCalories) {
        return maintenanceCalories + addedCaloriesFromObjective
    }

}
