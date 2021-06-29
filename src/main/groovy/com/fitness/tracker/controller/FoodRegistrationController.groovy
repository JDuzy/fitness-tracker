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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
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
    final UserService userService;

    @Autowired
    final FoodRegistrationService foodRegistrationService

    @Autowired
    final FoodService foodService

    @GetMapping("/food/registration")
    String getFoodRegistrations(Model model, @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate registrationDate){
        User loggedUser = userService.getPrincipal()
        model.addAttribute("user", loggedUser)
        List<FoodRegistration> dailyFoodsRegistrations = new ArrayList<FoodRegistration>()
        List<Food> foods = foodService.findAll()

        if (registrationDate == null){
            dailyFoodsRegistrations = foodRegistrationService.findAllFoodRegistrationByUserAndRegistrationDate(loggedUser, LocalDate.now())
        }
        else{
            dailyFoodsRegistrations = foodRegistrationService.findAllFoodRegistrationByUserAndRegistrationDate(loggedUser, registrationDate as LocalDate)
        }

        model.addAttribute("foodRegistrations", dailyFoodsRegistrations)
        model.addAttribute("foods", foods)
        "foodRegistration"
    }

    //Request body should have foodId: foodId, amount: amount
    @PostMapping("/food/registration")
    String registerAFood(Model model, @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate registrationDate, @RequestBody Map<String, String> payload){
        User loggedUser = userService.getPrincipal()
        //model.addAttribute("user", loggedUser)
        Long foodId = payload.get("foodId").toLong()
        BigDecimal amount = payload.get("amount").toBigDecimal()
        registrationDate ? foodRegistrationService.register(loggedUser, registrationDate, amount, foodId) : foodRegistrationService.register(loggedUser, LocalDate.now(), amount, foodId)

        "redirect:/food/registration"

    }
}