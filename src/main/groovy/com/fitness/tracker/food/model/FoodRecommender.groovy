package com.fitness.tracker.food.model

class FoodRecommender {

    List<Food> recommendBasedOnRemainingNutrients(List<Food> foods, Nutrients remainingNutrients){
        foods.findAll({food -> food.hasSameMainNutrient(remainingNutrients)})
    }
}
