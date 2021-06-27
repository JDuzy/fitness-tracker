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
class HomeController {

    @Autowired
    final UserService userService;

    @GetMapping("/")
    String index(){
        "index"
    }

    @GetMapping("/authenticated")
    String authenticated(Model model){
        model.addAttribute("user", userService.getPrincipal())
        "authenticated"
    }

    @GetMapping("/login")
    String login(){
        User user = userService.getPrincipal()
        if (user != null){
            return "redirect:/authenticated"
        }
        "login"
    }

}
