package com.fitness.tracker.person.controller

import com.fitness.tracker.person.model.Credentials
import com.fitness.tracker.person.service.CredentialsService
import com.fitness.tracker.person.service.PersonService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

import javax.validation.Valid


@Controller
@RequestMapping("/registration")
@CompileStatic
class RegistrationController {

    @Autowired
    final PersonService personService

    @Autowired
    final CredentialsService credentialsService

    @InitBinder
    void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true)
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor)
    }

    @GetMapping
    String register(@ModelAttribute Credentials credentials, Model model){
        Credentials principal = credentialsService.getPrincipal()
        if (principal != null){
            return "redirect:/food/registration"
        }
        model.addAttribute("credentials", credentials)
        "registration"
    }

    @PostMapping
    String saveRegistration(@Valid Credentials credentials, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        credentialsService.wasRegisteredValidly(credentials, bindingResult)
        if(bindingResult.hasErrors()){
            return "registration"
        }
        redirectAttributes.addFlashAttribute("message", "Succes! Your registration is now complete")
        credentialsService.register(credentials)
        "redirect:/login"
    }
}
