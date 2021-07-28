package com.fitness.tracker.food.repository

import com.fitness.tracker.person.model.Person
import com.fitness.tracker.food.model.FoodRegistration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.time.LocalDate

@Repository
interface FoodRegistrationRepository extends JpaRepository<FoodRegistration, Long> {


}