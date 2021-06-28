package com.fitness.tracker.service

import com.fitness.tracker.model.Food
import com.fitness.tracker.model.User
import com.fitness.tracker.model.registration.FoodRegistration
import com.fitness.tracker.repository.FoodRegistrationRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

import java.time.LocalDate

@Service
@CompileStatic
class FoodRegistrationService{

    @Autowired
    final FoodRegistrationRepository foodRegistrationRepository

    List<FoodRegistration> findAllFoodRegistrationByUserAndRegistrationDate(User user, LocalDate registrationDate) {
        foodRegistrationRepository.findAllFoodRegistrationByUserAndRegistrationDate(user, registrationDate)
    }
}
