package com.fitness.tracker.person.service

import com.fitness.tracker.food.model.Food
import com.fitness.tracker.food.model.FoodRegistration
import com.fitness.tracker.food.repository.FoodRegistrationRepository
import com.fitness.tracker.food.repository.FoodRepository
import com.fitness.tracker.exercise.model.Exercise
import com.fitness.tracker.exercise.model.ExerciseRegistration
import com.fitness.tracker.exercise.repository.ExerciseRepository
import com.fitness.tracker.security.Credentials
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.security.repository.CredentialsRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


import javax.transaction.Transactional
import java.time.LocalDate

import static org.springframework.http.HttpStatus.NOT_FOUND


@Service
@CompileStatic
class PersonService {

    @Autowired
    final FoodRepository foodRepository

    @Autowired
    final CredentialsRepository credentialsRepository

    @Autowired
    final ExerciseRepository exerciseRepository

    @Autowired
    final FoodRegistrationRepository foodRegistrationRepository

    Person getLoggedPerson(){
        Credentials credentials = null
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Credentials){
            credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        }

        Person person1 = Optional.ofNullable(credentials)
                .map({credentialsRepository.findById(it.id).map({it.person})})
                .orElseThrow( {new IllegalStateException("User not on DB")})
                .orElse(null)
        person1
    }

    @Transactional
    void updateData(Person person, Map<String, String> payload){
        person.updateData(payload.get("sex"), LocalDate.parse(payload.get("dateOfBirth")), payload.get("height").toInteger(), payload.get("weight").toBigDecimal(), payload.get("objective").toBigDecimal(), payload.get("physicalActivity").toBigDecimal())
    }

    @Transactional
    FoodRegistration registerFood(LocalDate registrationDate, BigDecimal amountOfGrams, Long foodId) {
        Person person = getLoggedPerson()
        Food food = foodRepository.findFoodById(foodId).orElseThrow({new IllegalStateException("Food with id ${foodId} does not exists")})
        FoodRegistration registration = new FoodRegistration( registrationDate: registrationDate, amountOfGrams: amountOfGrams, food: food)
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
    }

    Set<ExerciseRegistration> getExercisesRegistrationsByDate(LocalDate date) {
        Person person = getLoggedPerson()
        person.getExercisesRegistrationsByDate(date)
    }

    @Transactional
    void registerExercise(LocalDate registrationDate, BigDecimal time, BigDecimal weight, Long exerciseId) {
        Person person = getLoggedPerson()
        Exercise exercise = exerciseRepository.findExerciseById(exerciseId).orElseThrow({
            new IllegalStateException("Exercise with id ${exerciseId} does not exists")
        })
        ExerciseRegistration registration = new ExerciseRegistration(registrationDate: registrationDate, time: time, weight: weight, exercise: exercise)
        person.addExerciseRegistration(registration)
    }

    @Transactional
    void updateExerciseRegistration(Long registrationId, BigDecimal newTime, BigDecimal newWeight) {
        Person person = getLoggedPerson()
        println person.findExerciseRegistrationWithId(registrationId)
        ExerciseRegistration registration = Optional.ofNullable(person.findExerciseRegistrationWithId(registrationId) as ExerciseRegistration)
                .orElseThrow({
                    new ResponseStatusException(NOT_FOUND, "No exerciseRegistration with id: ${registrationId} was found")
                })
        person.deleteExerciseRegistration(registration)
        registration.time = newTime
        registration.weight = newWeight
        person.addExerciseRegistration(registration)
        registration
    }

    @Transactional
    void deleteExerciseRegistration(Long registrationId) {
        Person person = getLoggedPerson()
        ExerciseRegistration registration = Optional.ofNullable(person.findExerciseRegistrationWithId(registrationId) as ExerciseRegistration)
                .orElseThrow({
                    new ResponseStatusException(NOT_FOUND, "No exerciseRegistration with id: ${registrationId} was found")
                })
        person.deleteExerciseRegistration(registration)
    }

}
