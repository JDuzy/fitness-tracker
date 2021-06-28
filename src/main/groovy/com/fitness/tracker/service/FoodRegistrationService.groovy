package com.fitness.tracker.service

import com.fitness.tracker.model.Food
import com.fitness.tracker.model.User
import com.fitness.tracker.model.registration.FoodRegistration
import com.fitness.tracker.repository.FoodRegistrationRepository
import com.fitness.tracker.repository.FoodRepository
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

    @Autowired
    final FoodRepository foodRepository

    List<FoodRegistration> findAllFoodRegistrationByUserAndRegistrationDate(User user, LocalDate registrationDate) {
        foodRegistrationRepository.findAllFoodRegistrationByUserAndRegistrationDate(user, registrationDate)
    }

    FoodRegistration register(BigDecimal amount, Long foodId){
        Optional<Food> food = foodRepository.findFoodById(foodId)
        food.ifPresentOrElse()
        FoodRegistration registration= new FoodRegistration()
    }
}
