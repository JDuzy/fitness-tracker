package com.fitness.tracker.controller

import com.fitness.tracker.model.Food
import com.fitness.tracker.model.User
import com.fitness.tracker.model.registration.FoodRegistration
import com.fitness.tracker.service.FoodRegistrationService
import com.fitness.tracker.service.FoodService
import com.fitness.tracker.service.UserService
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


import java.time.LocalDate

import static org.springframework.format.annotation.DateTimeFormat.ISO.*


@Controller
@RequestMapping
@CompileStatic
class FoodRegistrationController {

    @Autowired
    final UserService userService

    @Autowired
    final FoodRegistrationService foodRegistrationService

    @Autowired
    final FoodService foodService

    @GetMapping("/food/registration")
    String getFoodRegistrations(Model model, @RequestParam @DateTimeFormat(iso = DATE) LocalDate registrationDate){
        User loggedUser = userService.getPrincipal()
        model.addAttribute("user", loggedUser)
        List<FoodRegistration> dailyFoodsRegistrations = new ArrayList<FoodRegistration>()
        List<Food> foods = foodService.findAll()

        dailyFoodsRegistrations = foodRegistrationService.findAllFoodRegistrationByUserAndRegistrationDate(loggedUser, registrationDate as LocalDate)

        model.addAttribute("foodRegistrations", dailyFoodsRegistrations)
        model.addAttribute("foods", foods)
        model.addAttribute("today", registrationDate.toString())
        model.addAttribute("yesterday", registrationDate.minusDays(1).toString())
        model.addAttribute("tomorrow", registrationDate.plusDays(1).toString())

        "foodRegistration"
    }

    //Request body should have foodId: foodId, amount: amount
    @PostMapping("/food/registration")
    String registerAFood(@RequestParam @DateTimeFormat(iso = DATE) LocalDate registrationDate, @RequestBody Map<String, String> payload){
        User loggedUser = userService.getPrincipal()
        Long foodId = payload.get("foodId").toLong()
        BigDecimal amount = payload.get("amount").toBigDecimal()
        foodRegistrationService.register(loggedUser, registrationDate, amount, foodId)

        "foodRegistration"
        //"redirect:/food/registration?registrationDate=${registrationDate.toString()}"
    }

    //Request body should have amount: amount
    @PutMapping("/food/registration/{registrationId}")
    String modifyARegistration(@PathVariable Long registrationId, @RequestParam @DateTimeFormat(iso = DATE) LocalDate registrationDate, @RequestBody Map<String, String> payload){
        BigDecimal amount = payload.get("amount").toBigDecimal()
        foodRegistrationService.update(registrationId, amount)

        "foodRegistration"
    }

    @DeleteMapping("/food/registration/{registrationId}")
    String deleteARegistration(@PathVariable Long registrationId, @RequestParam @DateTimeFormat(iso = DATE) LocalDate registrationDate){
        foodRegistrationService.deleteRegistrationById(registrationId)

        "foodRegistration"
    }
}
