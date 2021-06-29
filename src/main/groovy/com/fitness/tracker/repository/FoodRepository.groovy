package com.fitness.tracker.repository

import com.fitness.tracker.model.Food
import com.fitness.tracker.model.User
import com.fitness.tracker.model.registration.FoodRegistration
import org.springframework.data.jpa.repository.JpaRepository

interface FoodRepository extends JpaRepository< Food, Long> {

    Optional<Food> findFoodById(Long id);

}