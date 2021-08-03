package com.fitness.tracker.person.model

import com.fitness.tracker.exercise.model.Exercise
import groovy.transform.EqualsAndHashCode
import javax.validation.constraints.NotNull


@EqualsAndHashCode
class PhysicalObjective {

    @NotNull
    BigDecimal addedCaloriesFromObjective

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

    void changeObjective(BigDecimal newAddedCalories) {
        this.addedCaloriesFromObjective = newAddedCalories
    }

}
