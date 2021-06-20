package com.fitness.tracker.controller

import com.fitness.tracker.model.User
import groovy.transform.CompileStatic
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
@CompileStatic
class HomeController {

    @GetMapping("/")
    String index(){
        "index"
    }

    @GetMapping("/authenticated")
    String authenticated(Model model){
        model.addAttribute("user", getPrincipal())
    }

    @GetMapping("/login")
    String login(){
        User user = getPrincipal()
        if (user != null){
            return "authenticated"
        }
        "login"
    }

    private User getPrincipal(){
        User user = null
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User){
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        }
        user
    }
}
