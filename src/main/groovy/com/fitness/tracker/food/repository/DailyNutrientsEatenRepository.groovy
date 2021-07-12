package com.fitness.tracker.food.repository

import com.fitness.tracker.food.model.DailyNutrientsEaten
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.time.LocalDate

@Repository
interface DailyNutrientsEatenRepository extends JpaRepository< DailyNutrientsEaten, Long> {

    Optional<DailyNutrientsEaten> findByEatenDayAndPersonId(LocalDate eatenDay, Long personId)

}