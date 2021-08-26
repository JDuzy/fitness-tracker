package com.fitness.tracker.exercise.model

import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.model.PhysicalObjective

class ExerciseRecommender {

    static List<Exercise> recommendBasedOnPhysicalObjective(List<Exercise> exercises, PhysicalObjective objective){
        exercises.findAll({exercise -> objective.isExerciseOptimum(exercise)})
    }
}
