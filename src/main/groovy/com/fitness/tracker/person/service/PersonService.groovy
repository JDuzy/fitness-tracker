package com.fitness.tracker.person.service

import com.fitness.tracker.food.model.Food
import com.fitness.tracker.food.model.FoodRegistration
import com.fitness.tracker.food.repository.FoodRegistrationRepository
import com.fitness.tracker.food.repository.FoodRepository
import com.fitness.tracker.person.model.Credentials
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.repository.CredentialsRepository
import com.fitness.tracker.person.repository.PersonRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


import javax.transaction.Transactional
import java.time.LocalDate


@Service
@CompileStatic
class PersonService {

    @Autowired
    final PersonRepository personRepository

    @Autowired
    final CredentialsRepository credentialsRepository

    @Autowired
    final FoodRepository foodRepository

    @Autowired
    final FoodRegistrationRepository foodRegistrationRepository

    Person getLoggedPerson(){
        Credentials credentials = null
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Credentials){
            credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        }

        Optional.ofNullable(credentials)
                .map({credentialsRepository.findById(it.id).map({it.person})})
                .orElseThrow( {new IllegalStateException("User not on DB")})
                .orElse(null)
    }


    Person update(Person person, Map<String, String> payload){
        person.updateData(payload.get("sex"), LocalDate.parse(payload.get("dateOfBirth")), payload.get("height").toInteger(), payload.get("weight").toBigDecimal(), payload.get("objective").toBigDecimal(), payload.get("physicalActivity").toBigDecimal())
    }

    @Transactional
    FoodRegistration registerFood(LocalDate registrationDate, BigDecimal amountOfGrams, Long foodId) {
        Person person = getLoggedPerson()
        Food food = foodRepository.findFoodById(foodId).orElseThrow({new IllegalStateException("Food with id ${foodId} does not exists")})
        FoodRegistration registration = new FoodRegistration(person: person, registrationDate: registrationDate, amountOfGrams: amountOfGrams, food: food)
        person.addFoodRegistration(registration, registrationDate)
        registration
    }

    @Transactional
    Set<FoodRegistration> getFoodRegistrationsByDate(Person person, LocalDate date){
        person.getFoodRegistrationsByDate(date)
    }

    @Transactional
    void updateFoodRegistration(Long registrationId, BigDecimal newAmount) {
        Person person = getLoggedPerson()
        person.updateFoodRegistrationWithId(registrationId, newAmount)
    }

    @Transactional
    void deleteFoodRegistration(Long registrationId) {
        Person person = getLoggedPerson()
        person.deleteFoodRegistrationWithId(registrationId)
        foodRegistrationRepository.deleteById(registrationId)
    }
}
