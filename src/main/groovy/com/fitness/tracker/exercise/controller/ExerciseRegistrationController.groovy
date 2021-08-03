package com.fitness.tracker.exercise.controller

import com.fitness.tracker.exercise.model.Exercise
import com.fitness.tracker.exercise.model.ExerciseRegistration

import com.fitness.tracker.exercise.service.ExerciseService
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.service.PersonService
import com.fitness.tracker.security.service.CredentialsService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

import java.time.LocalDate

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE


@Controller
@RequestMapping
@CompileStatic
class ExerciseRegistrationController {

    @Autowired
    final PersonService personService

    @Autowired
    final CredentialsService credentialsService

    @Autowired
    final ExerciseService exerciseService

    @GetMapping("/exercise/registration")
    String getExerciseRegistrations(Model model, @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now().toString()}") @DateTimeFormat(iso = DATE) LocalDate registrationDate){
        Person loggedPerson = personService.getLoggedPerson()
        List<Exercise> allExercises = exerciseService.findAll()
        List<Exercise> recommendedExercises = personService.getRecommendedExercises(allExercises)
        Set<ExerciseRegistration> dailyExercisesRegistrations = personService.getExercisesRegistrationsByDate(registrationDate)
        model.addAllAttributes("person": loggedPerson, "credentials": credentialsService.getPrincipal(), "exerciseRegistrations": dailyExercisesRegistrations, "exercises": allExercises, "recommendedExercises": recommendedExercises, "today": registrationDate.toString(), "yesterday": registrationDate.minusDays(1).toString(), "tomorrow": registrationDate.plusDays(1).toString())
        "exerciseRegistration"
    }

    //Request body should have exerciseId: exerciseId, amount: amount
    @PostMapping("/exercise/registration")
    @ResponseBody
    String registerAExercise(@RequestParam @DateTimeFormat(iso = DATE) LocalDate registrationDate, @RequestBody Map<String, String> payload){
        Long exerciseId = payload.get("exerciseId").toLong()
        BigDecimal time = payload.get("time").toBigDecimal()
        BigDecimal weight = payload.get("weight").toBigDecimal()
        personService.registerExercise(registrationDate, time, weight, exerciseId)
        "Exercise registered"
    }

    //Request body should have amount: amount
    @PutMapping("/exercise/registration/{registrationId}")
    @ResponseBody
    String modifyARegistration(@PathVariable Long registrationId, @RequestBody Map<String, String> payload){
        BigDecimal time = payload.get("time").toBigDecimal()
        BigDecimal weight = payload.get("weight").toBigDecimal()
        personService.updateExerciseRegistration(registrationId, time, weight)
        "Updated"
    }

    @DeleteMapping("/exercise/registration/{registrationId}")
    @ResponseBody
    String deleteARegistration(@PathVariable Long registrationId){
        personService.deleteExerciseRegistration(registrationId)
        "Deleted"
    }
}
