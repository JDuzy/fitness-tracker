package com.fitness.tracker.controller

import com.fitness.tracker.model.DailyNutrientsEaten
import com.fitness.tracker.model.Food
import com.fitness.tracker.model.Person
import com.fitness.tracker.model.registration.FoodRegistration
import com.fitness.tracker.service.DailyNutrientsEatenService
import com.fitness.tracker.service.FoodRegistrationService
import com.fitness.tracker.service.FoodService
import com.fitness.tracker.service.PersonService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import java.time.LocalDate

import static org.springframework.format.annotation.DateTimeFormat.ISO.*


@Controller
@RequestMapping
@CompileStatic
class FoodRegistrationController {

    @Autowired
    final PersonService personService

    @Autowired
    final FoodRegistrationService foodRegistrationService

    @Autowired
    final FoodService foodService

    @Autowired
    final DailyNutrientsEatenService dailyNutrientsEatenService

    @GetMapping("/food/registration")
    String getFoodRegistrations(Model model, @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now().toString()}") @DateTimeFormat(iso = DATE) LocalDate registrationDate){
        Person loggedPerson = personService.getPrincipal()
        model.addAttribute("person", loggedPerson)

        dailyNutrientsEatenService.updateActualNutrientsEatenByEatenDayAndPerson(registrationDate, loggedPerson)

        List<Food> foods = foodService.findAll()
        List<FoodRegistration> dailyFoodsRegistrations = foodRegistrationService.findAllFoodRegistrationByPersonAndRegistrationDate(loggedPerson, registrationDate)

        //TO DO: Use model.addAttributes in 1 line
        model.addAttribute("foodRegistrations", dailyFoodsRegistrations)
        model.addAttribute("foods", foods)
        model.addAttribute("today", registrationDate.toString())
        model.addAttribute("yesterday", registrationDate.minusDays(1).toString())
        model.addAttribute("tomorrow", registrationDate.plusDays(1).toString())

        "foodRegistration"
    }

    //Request body should have foodId: foodId, amount: amount
    @PostMapping("/food/registration")
    @ResponseBody
    String registerAFood(@RequestParam @DateTimeFormat(iso = DATE) LocalDate registrationDate, @RequestBody Map<String, String> payload){
        Person loggedPerson = personService.getPrincipal()
        Long foodId = payload.get("foodId").toLong()
        BigDecimal amountOfGrams = payload.get("amount").toBigDecimal()
        foodRegistrationService.register(loggedPerson, registrationDate, amountOfGrams, foodId)
        "Food registered"
    }

    //Request body should have amount: amount
    @PutMapping("/food/registration/{registrationId}")
    @ResponseBody
    String modifyARegistration(@PathVariable Long registrationId, @RequestBody Map<String, String> payload){
        Person loggedPerson = personService.getPrincipal()
        BigDecimal amountOfGrams = payload.get("amount").toBigDecimal()
        foodRegistrationService.update(registrationId, amountOfGrams, loggedPerson)
        "Updated"
    }

    @DeleteMapping("/food/registration/{registrationId}")
    @ResponseBody
    String deleteARegistration(@PathVariable Long registrationId){
        Person loggedPerson = personService.getPrincipal()
        foodRegistrationService.deleteRegistrationById(registrationId, loggedPerson)
        "Deleted"
    }
}
