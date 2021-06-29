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

import javax.transaction.Transactional
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

    @Transactional
    FoodRegistration register(User user, LocalDate registrationDate, BigDecimal amount, Long foodId){
        Optional<Food> food = foodRepository.findFoodById(foodId)
        if (food.isEmpty()){
            throw new IllegalStateException("Food with id ${foodId} does not exists")
        }
        FoodRegistration registration = new FoodRegistration(user: user, registrationDate: registrationDate, amount: amount, food: food.get())
        System.out.println(user.toString())
        foodRegistrationRepository.save(registration)
    }
}
