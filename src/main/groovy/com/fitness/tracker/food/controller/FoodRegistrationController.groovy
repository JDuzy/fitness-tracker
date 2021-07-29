package com.fitness.tracker.food.controller

import com.fitness.tracker.food.model.Food
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.food.model.FoodRegistration
import com.fitness.tracker.food.service.FoodService
import com.fitness.tracker.security.service.CredentialsService
import com.fitness.tracker.person.service.PersonService
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
    final FoodService foodService

    @Autowired
    final CredentialsService credentialsService

    @GetMapping("/food/registration")
    String getFoodRegistrations(Model model, @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now().toString()}") @DateTimeFormat(iso = DATE) LocalDate registrationDate){
        Person loggedPerson = personService.getLoggedPerson()
        List<Food> foods = foodService.findAll()
        Set<FoodRegistration> dailyFoodsRegistrations = personService.getFoodRegistrationsByDate(loggedPerson, registrationDate)
        model.addAllAttributes([ "credentials":credentialsService.getPrincipal(), "person":loggedPerson, "foodRegistrations":dailyFoodsRegistrations, "foods":foods ,"today":registrationDate, "yesterday":registrationDate.minusDays(1).toString(), "tomorrow":registrationDate.plusDays(1).toString()])
        "foodRegistration"
    }

    //Request body should have foodId: foodId, amount: amount
    @PostMapping("/food/registration")
    @ResponseBody
    String registerAFood(@RequestParam @DateTimeFormat(iso = DATE) LocalDate registrationDate, @RequestBody Map<String, String> payload){
        Long foodId = payload.get("foodId").toLong()
        BigDecimal amountOfGrams = payload.get("amount").toBigDecimal()
        personService.registerFood(registrationDate, amountOfGrams, foodId)
        "Food registered"
    }

    //Request body should have amount: amount
    @PutMapping("/food/registration/{registrationId}")
    @ResponseBody
    String modifyARegistration(@PathVariable Long registrationId, @RequestBody Map<String, String> payload){
        BigDecimal amountOfGrams = payload.get("amount").toBigDecimal()
        personService.updateFoodRegistration(registrationId, amountOfGrams)
        "Updated"
    }

    @DeleteMapping("/food/registration/{registrationId}")
    @ResponseBody
    String deleteARegistration(@PathVariable Long registrationId){
        personService.deleteFoodRegistration(registrationId)
        "Deleted"
    }
}
