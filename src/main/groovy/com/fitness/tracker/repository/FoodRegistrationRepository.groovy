package com.fitness.tracker.repository

import com.fitness.tracker.model.Credentials
import com.fitness.tracker.model.Food
import com.fitness.tracker.model.User
import com.fitness.tracker.model.registration.FoodRegistration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.time.LocalDate

@Repository
interface FoodRegistrationRepository extends JpaRepository<FoodRegistration, Long> {

    List<FoodRegistration> findAllFoodRegistrationByUserAndRegistrationDate(User user, LocalDate localDate)
}