package com.fitness.tracker.weight.controller

import com.fitness.tracker.weight.model.WeightRegistration
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
class WeightRegistrationController {

    @Autowired
    final PersonService personService

    @GetMapping("/weight/registration")
    String getWeightRegistrations(Model model, @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now().toString()}") @DateTimeFormat(iso = DATE) LocalDate registrationDate){
        Person loggedPerson = personService.getLoggedPerson()
        model.addAttribute("person", loggedPerson)

        List<WeightRegistration> dailyWeightRegistrations = personService.getWeightRegistrationsByDate(registrationDate)

        //TO DO: Use model.addAttributes in 1 line
        model.addAttribute("weightRegistrations", dailyWeightRegistrations)
        model.addAttribute("thisMonth", registrationDate.getMonth().toString().toLowerCase())
        model.addAttribute("thisYear", registrationDate.getYear().toString())
        model.addAttribute("lastMonth", registrationDate.minusMonths(1).getMonth().toString().toLowerCase())
        model.addAttribute("nextMonth", registrationDate.plusMonths(1).getMonth().toString().toLowerCase())

        "weightRegistration"
    }

    @PostMapping("/weight/registration")
    @ResponseBody
    String registerAWeight(@RequestParam @DateTimeFormat(iso = DATE) LocalDate registrationDate, @RequestBody Map<String, String> payload){
        Long weightId = payload.get("weightId").toLong()
        BigDecimal time = payload.get("time").toBigDecimal()
        BigDecimal weight = payload.get("weight").toBigDecimal()
        personService.registerWeight(registrationDate, weight)
        "Weight registered"
    }

    @PutMapping("/weight/registration/{registrationId}")
    @ResponseBody
    String modifyARegistration(@PathVariable Long registrationId, @RequestBody Map<String, String> payload){
        BigDecimal time = payload.get("time").toBigDecimal()
        BigDecimal weight = payload.get("weight").toBigDecimal()
        personService.updateWeightRegistration(registrationId, weight)
        "Updated"
    }

    @DeleteMapping("/weight/registration/{registrationId}")
    @ResponseBody
    String deleteARegistration(@PathVariable Long registrationId){
        personService.deleteWeightRegistration(registrationId)
        "Deleted"
    }
}
