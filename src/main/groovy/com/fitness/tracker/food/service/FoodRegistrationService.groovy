package com.fitness.tracker.food.service

import com.fitness.tracker.food.model.DailyNutrientsEaten
import com.fitness.tracker.food.model.Food
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.food.model.FoodRegistration
import com.fitness.tracker.food.repository.FoodRegistrationRepository
import com.fitness.tracker.food.repository.FoodRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

import javax.transaction.Transactional
import java.time.LocalDate

import static org.springframework.http.HttpStatus.NOT_FOUND

@Service
@CompileStatic
class FoodRegistrationService{

    @Autowired
    final FoodRegistrationRepository foodRegistrationRepository

    @Autowired
    final FoodRepository foodRepository

    @Autowired
    final DailyNutrientsEatenService dailyNutrientsEatenService

    List<FoodRegistration> findAllFoodRegistrationByPersonAndRegistrationDate(Person person, LocalDate registrationDate) {
        foodRegistrationRepository.findAllFoodRegistrationByPersonAndRegistrationDate(person, registrationDate)
    }


}
