package com.fitness.tracker.person.controller

import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.service.PersonService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ResponseBody


@Controller
@RequestMapping
@CompileStatic
class PersonDataController {

    @Autowired
    PersonService personService

    @GetMapping("/person/update")
    String getUpdateDataForm(Model model){
        Person loggedPerson = personService.getPrincipal()
        model.addAttribute("person", loggedPerson)
        "updateData"
    }

    @PutMapping("/person/update")
    @ResponseBody
    String putUpdateDataForm(@RequestBody Map<String, String> payload){
        Person loggedPerson = personService.getPrincipal()
        personService.update(loggedPerson, payload)

        "Updated"
    }
}
