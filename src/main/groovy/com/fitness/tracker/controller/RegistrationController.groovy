package com.fitness.tracker.controller

import com.fitness.tracker.model.User
import com.fitness.tracker.service.UserService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
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
    final UserService userService

    @InitBinder
    void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true)
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor)
    }

    @GetMapping
    String register(@ModelAttribute User user,Model model){
        User principal = userService.getPrincipal()
        if (principal != null){
            return "redirect:/food/registration"
        }
        model.addAttribute("user", user)
        "registration"
    }

    @PostMapping
    String saveRegistration(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        //checks if the user exists
        if (userService.userExists(user.credentials.email)){
            bindingResult.addError(new FieldError("user", "credentials.email", "Email adress already in use"))
        }

        //checks if password match
        if (!user.credentials.passwordsMatch()){
            bindingResult.addError(new FieldError("user", "credentials.rpassword", "Passwords must match"))
        }

        //Checks if weekly objective has a selected value
        if (user.weightChangePerWeek == -1){
            bindingResult.addError(new FieldError("user", "weightChangePerWeek", "Please select a weekly objective"))
        }

        if(bindingResult.hasErrors()){
            return "registration"
        }
        redirectAttributes.addFlashAttribute("message", "Succes! Your registration is now complete")
        userService.register(user)
        "redirect:/login"
    }
}
