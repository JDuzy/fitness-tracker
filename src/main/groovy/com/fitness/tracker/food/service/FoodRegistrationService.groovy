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

    @Transactional
    FoodRegistration register(Person person, LocalDate registrationDate, BigDecimal amountOfGrams, Long foodId){
        Optional<Food> food = foodRepository.findFoodById(foodId)
        food.orElseThrow({
            new IllegalStateException("Food with id ${foodId} does not exists")
        })
        FoodRegistration registration = new FoodRegistration(person: person, registrationDate: registrationDate, amountOfGrams: amountOfGrams, food: food.get())
        dailyNutrientsEatenService.updateActualNutrientsEatenByEatenDayAndPerson(registrationDate, person)
        DailyNutrientsEaten registrationDateNutrientsEaten = person.addFoodRegistration(registration)
        dailyNutrientsEatenService.save(registrationDateNutrientsEaten)
        foodRegistrationRepository.save(registration)

    }

    @Transactional
    FoodRegistration update(long registrationId, BigDecimal newAmount, Person person) {
        Optional<FoodRegistration> foodRegistration = foodRegistrationRepository.findById(registrationId)
        foodRegistration.orElseThrow({
            new ResponseStatusException(NOT_FOUND, "No foodRegistration with id: ${registrationId} was found")
        })
        FoodRegistration registration = foodRegistration.get()
        dailyNutrientsEatenService.updateActualNutrientsEatenByEatenDayAndPerson(registration.registrationDate, person)
        person.deleteFoodRegistration(registration)
        registration.amountOfGrams = newAmount
        DailyNutrientsEaten registrationDateNutrientsEaten = person.addFoodRegistration(registration)
        dailyNutrientsEatenService.save(registrationDateNutrientsEaten)
        foodRegistrationRepository.save(registration)
    }

    @Transactional
    void deleteRegistrationById(long id, Person person) {
        Optional<FoodRegistration> registration =  foodRegistrationRepository.findFoodRegistrationById(id)
        registration.orElseThrow({
          new ResponseStatusException(NOT_FOUND, "No foodRegistration with id: ${id} was found")
        })
        dailyNutrientsEatenService.updateActualNutrientsEatenByEatenDayAndPerson(registration.get().registrationDate, person)
        DailyNutrientsEaten registrationDateNutrientsEaten = person.deleteFoodRegistration(registration.get())
        dailyNutrientsEatenService.save(registrationDateNutrientsEaten)
        foodRegistrationRepository.deleteById(id)
    }
}
