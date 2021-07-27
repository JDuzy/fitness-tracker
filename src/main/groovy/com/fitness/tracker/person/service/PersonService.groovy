package com.fitness.tracker.person.service

import com.fitness.tracker.food.model.DailyNutrientsEaten
import com.fitness.tracker.food.model.Food
import com.fitness.tracker.food.model.FoodRegistration
import com.fitness.tracker.food.repository.FoodRegistrationRepository
import com.fitness.tracker.food.repository.FoodRepository
import com.fitness.tracker.food.service.DailyNutrientsEatenService
import com.fitness.tracker.person.model.Credentials
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.repository.CredentialsRepository
import com.fitness.tracker.person.repository.PersonRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.server.ResponseStatusException

import javax.transaction.Transactional
import java.time.LocalDate
import java.util.function.Supplier

import static org.springframework.http.HttpStatus.NOT_FOUND


@Service
@CompileStatic
class PersonService {

    @Autowired
    final PersonRepository personRepository

    @Autowired
    final CredentialsRepository credentialsRepository

    @Autowired
    final FoodRepository foodRepository

    /*@Autowired
    final DailyNutrientsEatenService dailyNutrientsEatenService*/

    @Autowired
    final FoodRegistrationRepository foodRegistrationRepository


    /*boolean emailUsedExists(Person person){
        credentialsRepository.findCredentialsByEmail(person.email).isPresent()
    }*/

    Person getPrincipal(){
        Credentials credentials = null
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Credentials){
            credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        }

        Optional.ofNullable(credentials)
                .map({credentialsRepository.findById(it.id).map({it.person})})
                .orElseThrow( {new IllegalStateException("User not on DB")})
                .orElse(null)
    }

    /*@Transactional
    Person register(Person person){
        if (emailUsedExists(person)){
            throw new IllegalStateException("Email already taken")
        }
        String encodedPassword = bCryptPasswordEncoder.encode(person.password)
        person.password = encodedPassword
        person.setNutritionalObjective()
        personRepository.save(person)
    }*/

    Person update(Person person, Map<String, String> payload){
        person.sex = payload.get("sex")
        person.dateOfBirth = LocalDate.parse(payload.get("dateOfBirth"))
        person.height = payload.get("height").toInteger()
        person.weight = payload.get("weight").toBigDecimal()
        person.weightChangePerWeek = payload.get("objective").toBigDecimal()
        person.physicalActivity = payload.get("physicalActivity").toBigDecimal()
        person.setNutritionalObjective()
    }

    /*@Transactional
    Optional<Person> findPersonByEmail(String email){
        *//*Optional<Credentials> credentials = credentialsRepository.findCredentialsByEmail(email)
        Optional<Person> person = Optional.empty()
        credentials.ifPresent({ person = personRepository.findPersonByCredentials(credentials.get())})
        person*//*
        credentialsRepository.findCredentialsByEmail(email).map({personRepository.findPersonByCredentials(it)}).orElse(Optional.empty())
    }*/


    /*void wasRegisteredValidly(Person person, BindingResult bindingResult) {
        if (emailUsedExists(person)){
            bindingResult.addError(new FieldError("user", "credentials.email", "Email adress already in use"))
        }

        if (!person.passwordsMatch()){
            bindingResult.addError(new FieldError("user", "credentials.rpassword", "Passwords must match"))
        }
    }*/

    @Transactional
    FoodRegistration registerFood(LocalDate registrationDate, BigDecimal amountOfGrams, Long foodId) {
        Person person = getPrincipal()
        Food food = foodRepository.findFoodById(foodId).orElseThrow({new IllegalStateException("Food with id ${foodId} does not exists")})
        FoodRegistration registration = new FoodRegistration(person: person, registrationDate: registrationDate, amountOfGrams: amountOfGrams, food: food)
        //dailyNutrientsEatenService.updateActualNutrientsEatenByEatenDayAndPerson(registrationDate, person)
        person.addFoodRegistration(registration, registrationDate)
        registration
    }

    @Transactional
    Set<FoodRegistration> getFoodRegistrationsByDate(Person person, LocalDate date){
        person.getFoodRegistrationsByDate(date)
    }

    @Transactional
    void updateFoodRegistration(Long registrationId, BigDecimal newAmount) {
        Person person = getPrincipal()
        person.updateFoodRegistrationWithId(registrationId, newAmount)
    }

    @Transactional
    void deleteFoodRegistration(Long registrationId) {
        Person person = getPrincipal()
        person.deleteFoodRegistrationWithId(registrationId)
        /*FoodRegistration registration =  Optional.ofNullable(person.findFoodRegistrationWithId(registrationId) as FoodRegistration).orElseThrow({
            new ResponseStatusException(NOT_FOUND, "No foodRegistration with id: ${registrationId} was found")
        })
        dailyNutrientsEatenService.updateActualNutrientsEatenByEatenDayAndPerson(registration.registrationDate, person)
        person.deleteFoodRegistration(registration, registration.registrationDate)*/
        foodRegistrationRepository.deleteById(registrationId)
    }
}
