package com.fitness.tracker.exercise.controller

import com.fitness.tracker.exercise.model.Exercise
import com.fitness.tracker.exercise.model.ExerciseRegistration
import com.fitness.tracker.exercise.service.ExerciseRegistrationService
import com.fitness.tracker.exercise.service.ExerciseService
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.service.PersonService
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
    final ExerciseRegistrationService exerciseRegistrationService

    @Autowired
    final ExerciseService exerciseService

    @GetMapping("/exercise/registration")
    String getExerciseRegistrations(Model model, @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now().toString()}") @DateTimeFormat(iso = DATE) LocalDate registrationDate){
        Person loggedPerson = personService.getPrincipal()
        model.addAttribute("person", loggedPerson)

        List<Exercise> exercises = exerciseService.findAll()
        List<ExerciseRegistration> dailyExercisesRegistrations = exerciseRegistrationService.findAllExerciseRegistrationByPersonAndRegistrationDate(loggedPerson, registrationDate)

        //TO DO: Use model.addAttributes in 1 line
        model.addAttribute("exerciseRegistrations", dailyExercisesRegistrations)
        model.addAttribute("exercises", exercises)
        model.addAttribute("today", registrationDate.toString())
        model.addAttribute("yesterday", registrationDate.minusDays(1).toString())
        model.addAttribute("tomorrow", registrationDate.plusDays(1).toString())

        "exerciseRegistration"
    }

    //Request body should have exerciseId: exerciseId, amount: amount
    @PostMapping("/exercise/registration")
    @ResponseBody
    String registerAExercise(@RequestParam @DateTimeFormat(iso = DATE) LocalDate registrationDate, @RequestBody Map<String, String> payload){
        Person loggedPerson = personService.getPrincipal()
        Long exerciseId = payload.get("exerciseId").toLong()
        BigDecimal time = payload.get("time").toBigDecimal()
        exerciseRegistrationService.register(loggedPerson, registrationDate, time, exerciseId)
        "Exercise registered"
    }

    //Request body should have amount: amount
    @PutMapping("/exercise/registration/{registrationId}")
    @ResponseBody
    String modifyARegistration(@PathVariable Long registrationId, @RequestBody Map<String, String> payload){
        Person loggedPerson = personService.getPrincipal()
        BigDecimal time = payload.get("time").toBigDecimal()
        exerciseRegistrationService.update(registrationId, time, loggedPerson)
        "Updated"
    }

    @DeleteMapping("/exercise/registration/{registrationId}")
    @ResponseBody
    String deleteARegistration(@PathVariable Long registrationId){
        Person loggedPerson = personService.getPrincipal()
        exerciseRegistrationService.deleteRegistrationById(registrationId, loggedPerson)
        "Deleted"
    }
}