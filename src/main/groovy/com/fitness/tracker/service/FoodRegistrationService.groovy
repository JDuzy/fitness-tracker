package com.fitness.tracker.service

import com.fitness.tracker.model.Food
import com.fitness.tracker.model.Person
import com.fitness.tracker.model.registration.FoodRegistration
import com.fitness.tracker.repository.FoodRegistrationRepository
import com.fitness.tracker.repository.FoodRepository
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

    List<FoodRegistration> findAllFoodRegistrationByPersonAndRegistrationDate(Person person, LocalDate registrationDate) {
        foodRegistrationRepository.findAllFoodRegistrationByPersonAndRegistrationDate(person, registrationDate)
    }

    @Transactional
    FoodRegistration register(Person person, LocalDate registrationDate, BigDecimal amount, Long foodId){
        Optional<Food> food = foodRepository.findFoodById(foodId)
        if (food.isEmpty()){
            throw new IllegalStateException("Food with id ${foodId} does not exists")
        }
        FoodRegistration registration = new FoodRegistration(person: person, registrationDate: registrationDate, amount: amount, food: food.get())
        //person.eatFood(food)
        foodRegistrationRepository.save(registration)
    }

    @Transactional
    FoodRegistration update(long registrationId, BigDecimal newAmount) {
        Optional<FoodRegistration> foodRegistration = foodRegistrationRepository.findById(registrationId)
        foodRegistration.orElseThrow({
            new ResponseStatusException(NOT_FOUND, "No foodRegistration with id: ${registrationId} was found")
        })
        FoodRegistration registration = foodRegistration.get()
        registration.amount = newAmount
        foodRegistrationRepository.save(registration)
    }


    void deleteRegistrationById(long id) {
        foodRegistrationRepository.deleteById(id)
    }
}
