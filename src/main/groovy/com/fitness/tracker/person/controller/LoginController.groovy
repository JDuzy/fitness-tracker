package com.fitness.tracker.person.controller

import com.fitness.tracker.person.model.Credentials
import com.fitness.tracker.person.service.CredentialsService
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
    final CredentialsService credentialsService

    @GetMapping("/")
    String index(){
        "redirect:/food/registration?registrationDate=${LocalDate.now().toString()}"
    }

    @GetMapping("/login")
    String login(){
        Credentials credentials = credentialsService.getPrincipal()
        if (credentials != null){
            return "redirect:/food/registration?registrationDate=${LocalDate.now().toString()}"
        }
        "login"
    }

}
