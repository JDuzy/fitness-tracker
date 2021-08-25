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


import javax.transaction.Transactional
import java.time.LocalDate




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

        Person person = Optional.ofNullable(credentials)
                .map({credentialsRepository.findById(it.id).map({it.person})})
                .orElseThrow( {new IllegalStateException("User not on DB")})
                .orElse(null)
        person
    }

    @Transactional
    void updateData(Map<String, String> payload){
        loggedPerson.updateData(payload.get("sex"), LocalDate.parse(payload.get("dateOfBirth")), payload.get("height").toInteger(), payload.get("weight").toBigDecimal(), payload.get("objective").toBigDecimal(), payload.get("physicalActivity").toBigDecimal())
    }

    @Transactional
    FoodRegistration registerFood(LocalDate registrationDate, BigDecimal amountOfGrams, Long foodId) {
        Food food = foodRepository.findFoodById(foodId).orElseThrow({new IllegalStateException("Food with id ${foodId} does not exists")})
        FoodRegistration registration = new FoodRegistration( registrationDate: registrationDate, amountOfGrams: amountOfGrams, food: food)
        loggedPerson.addFoodRegistration(registration, registrationDate)
        registration
    }

    @Transactional
    Set<FoodRegistration> getFoodRegistrationsByDate(LocalDate date){
        loggedPerson.getFoodRegistrationsByDate(date)
    }

    @Transactional
    void updateFoodRegistration(Long registrationId, BigDecimal newAmount) {
        loggedPerson.updateFoodRegistrationWithId(registrationId, newAmount)
    }

    @Transactional
    void deleteFoodRegistration(Long registrationId) {
        loggedPerson.deleteFoodRegistrationWithId(registrationId)
    }

    @Transactional
    Set<ExerciseRegistration> getExercisesRegistrationsByDate(LocalDate date) {
        loggedPerson.getExercisesRegistrationsByDate(date)
    }

    @Transactional
    void registerExercise(LocalDate registrationDate, BigDecimal time, BigDecimal weight, Long exerciseId) {
        Exercise exercise = exerciseRepository.findExerciseById(exerciseId).orElseThrow({
            new IllegalStateException("Exercise with id ${exerciseId} does not exists")
        })
        ExerciseRegistration registration = new ExerciseRegistration(registrationDate: registrationDate, time: time, weight: weight, exercise: exercise)
        loggedPerson.addExerciseRegistration(registration)
    }

    @Transactional
    void updateExerciseRegistration(Long registrationId, BigDecimal newTime, BigDecimal newWeight) {
        loggedPerson.updateExerciseRegistrationWithId(registrationId, newTime, newWeight)
    }

    @Transactional
    void deleteExerciseRegistration(Long registrationId) {
        loggedPerson.deleteExerciseRegistrationWithId(registrationId)
    }

    List<Food> getRecommendedFoods(List<Food> allFoods, LocalDate date) {
        loggedPerson.receiveFoodRecommendations(allFoods, date)
    }

    List<Exercise> getRecommendedExercises(List<Exercise> allExercises) {
        loggedPerson.receiveExerciseRecommendations(allExercises)
    }
}
