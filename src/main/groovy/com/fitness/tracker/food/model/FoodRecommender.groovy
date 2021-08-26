package com.fitness.tracker.food.model

class FoodRecommender {

    static List<Food> recommendBasedOnRemainingNutrients(List<Food> foods, Nutrients remainingNutrients){
        foods.findAll({food -> food.hasSimilarNutrientDistributionTo(remainingNutrients)})
    }
}
