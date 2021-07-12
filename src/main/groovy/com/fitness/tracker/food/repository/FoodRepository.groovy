package com.fitness.tracker.food.repository

import com.fitness.tracker.food.model.Food
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FoodRepository extends JpaRepository< Food, Long> {

    Optional<Food> findFoodById(Long id);

}