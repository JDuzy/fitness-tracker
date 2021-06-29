package com.fitness.tracker.controller

import com.fitness.tracker.model.User
import com.fitness.tracker.service.UserService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
@CompileStatic
class LoginController {

    @Autowired
    final UserService userService;

    @GetMapping("/")
    String index(){
        "redirect:/food/registration"
    }

    @GetMapping("/login")
    String login(){
        User user = userService.getPrincipal()
        if (user != null){
            return "redirect:/food/registration"
        }
        "login"
    }

    @GetMapping("/authenticated")
    String authenticated(){
        "redirect:/food/registration"
    }

}