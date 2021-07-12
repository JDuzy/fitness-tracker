package com.fitness.tracker.person.controller

import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.service.PersonService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

import java.time.LocalDate

@Controller
@RequestMapping
@CompileStatic
class LoginController {

    @Autowired
    final PersonService personService

    @GetMapping("/")
    String index(){
        "redirect:/food/registration?registrationDate=${LocalDate.now().toString()}"
    }

    @GetMapping("/login")
    String login(){
        Person person = personService.getPrincipal()
        if (person != null){
            return "redirect:/food/registration?registrationDate=${LocalDate.now().toString()}"
        }
        "login"
    }

}
