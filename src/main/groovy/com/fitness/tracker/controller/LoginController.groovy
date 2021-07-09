package com.fitness.tracker.controller

import com.fitness.tracker.model.Person
import com.fitness.tracker.service.PersonService
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
    final PersonService userService

    @GetMapping("/")
    String index(){
        "redirect:/food/registration?registrationDate=${LocalDate.now().toString()}"
    }

    @GetMapping("/login")
    String login(){
        Person user = userService.getPrincipal()
        if (user != null){
            return "redirect:/food/registration?registrationDate=${LocalDate.now().toString()}"
        }
        "login"
    }

}
