package com.fitness.tracker.controller

import com.fitness.tracker.model.Credentials
import com.fitness.tracker.model.Person
import com.fitness.tracker.model.registration.FoodRegistration
import com.fitness.tracker.repository.FoodRegistrationRepository
import com.fitness.tracker.repository.PersonRepository
import com.fitness.tracker.service.FoodRegistrationService
import com.fitness.tracker.service.PersonService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.http.MediaType;
import java.time.LocalDate
import static org.mockito.Mockito.when


@SpringBootTest
@AutoConfigureMockMvc
class FoodRegistrationControllerTest {

    @Autowired
    MockMvc mockMvc

    @MockBean
    PersonService personService

    @Autowired
    PersonRepository personRepository

    Person personUsedToTest

    @Autowired
    FoodRegistrationRepository foodRegistrationRepository

    @Autowired
    FoodRegistrationService foodRegistrationService

    @Autowired
    BCryptPasswordEncoder passwordEncoder


    @BeforeEach
    void setUp(){
        //Set up credentials for the person
        String password = passwordEncoder.encode("123456")
        Credentials credentials = new Credentials(userName: "testUser", email: "testUser@mail.com", password: password, rpassword: password);

        //Set up the Person
        //DailyNutrientsEaten ints created on the attribute
        LocalDate dob = LocalDate.now().minusYears(18)
        Person person = new Person(credentials: credentials, dateOfBirth: dob, weight: 80, height: 180, sex: "male", physicalActivity: 1.725, weightChangePerWeek: 150)
        person.setNutritionalObjective()
        personUsedToTest = person
        personRepository.save(person)

        when(personService.getPrincipal()).thenReturn(person)
    }

    @AfterEach
    void tearDown(){
        foodRegistrationRepository.deleteAll()
        personRepository.delete(personUsedToTest)
    }


    @Test
    void registerSomeFoodsForToday(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        Person loggedUser = personService.getPrincipal()
        Integer bananaCaloriesPer100g = 84
        Integer appleCaloriesPer80g = 72
        Integer remainingCaloriesBeforeRegistrations = loggedUser.calculateRemainingCalories()
        Integer objectiveCalories = loggedUser.objectiveCalories

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post(("/food/registration?registrationDate=${LocalDate.now().toString()}"))
                .with(SecurityMockMvcRequestPostProcessors.user(loggedUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"1\", \"amount\": \"100\" }")
        )

        mockMvc.perform(MockMvcRequestBuilders.post(("/food/registration?registrationDate=${LocalDate.now().toString()}"))
                .with(SecurityMockMvcRequestPostProcessors.user(loggedUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"2\", \"amount\": \"160\" }")
        )

        Integer remainingCaloriesAfterRegistrations = loggedUser.calculateRemainingCalories()
        Integer eatenCaloriesAfterRegistration = loggedUser.eatenCalories

        //THEN
        Assertions.assertEquals(bananaCaloriesPer100g + appleCaloriesPer80g * 2, eatenCaloriesAfterRegistration)
        Assertions.assertEquals(remainingCaloriesBeforeRegistrations - remainingCaloriesAfterRegistrations, eatenCaloriesAfterRegistration)
        Assertions.assertEquals(objectiveCalories - remainingCaloriesAfterRegistrations, eatenCaloriesAfterRegistration)
    }

    @Test
    void deleteAFoodRegistrationForToday(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        Person loggedUser = personService.getPrincipal()

        FoodRegistration registrationToDelete = foodRegistrationService.register(loggedUser, LocalDate.now(), 100.0, 1) //100g of banana
        foodRegistrationService.register(loggedUser, LocalDate.now(), 80.0, 2) //80g of apple

        Integer initialCaloriesEaten = loggedUser.eatenCalories
        Integer caloriesToDelete = registrationToDelete.calories
        Integer remainingCaloriesBeforeDelete = loggedUser.calculateRemainingCalories()

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete(("/food/registration/${registrationToDelete.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(loggedUser))
        )

        Integer remainingCaloriesAfterDelete = loggedUser.calculateRemainingCalories()
        Integer eatenCaloriesAfterDelete = loggedUser.eatenCalories


        //THEN
        Assertions.assertEquals(initialCaloriesEaten - caloriesToDelete, eatenCaloriesAfterDelete)
        Assertions.assertEquals(remainingCaloriesBeforeDelete  + caloriesToDelete, remainingCaloriesAfterDelete)
        Assertions.assertEquals(loggedUser.objectiveCalories - remainingCaloriesAfterDelete, eatenCaloriesAfterDelete)
    }

    @Test
    void updateAFoodRegistrationForToday(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        Person loggedUser = personService.getPrincipal()
        BigDecimal bananaOldAmountOfGrams = 100
        BigDecimal bananaNewAmountOfGrams = 100
        BigDecimal appleAmountOfGrams = 80

        FoodRegistration bananaRegistrationToUpdate = foodRegistrationService.register(loggedUser, LocalDate.now(), bananaOldAmountOfGrams, 1) //100g of banana
        FoodRegistration appleRegistrationWhichStays = foodRegistrationService.register(loggedUser, LocalDate.now(), appleAmountOfGrams, 2) //80g of apple

        Integer initialCaloriesEaten = loggedUser.eatenCalories
        Integer caloriesOfBananaRegistrationBeforeUpdate = bananaRegistrationToUpdate.calories
        Integer caloriesOfAppleRegistrationWhichStays = appleRegistrationWhichStays.calories
        Integer remainingCaloriesBeforeDelete = loggedUser.calculateRemainingCalories()

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.put(("/food/registration/${bananaRegistrationToUpdate.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(loggedUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"1\", \"amount\": \"${bananaNewAmountOfGrams}\" }")
        )

        Integer caloriesOfBananaRegistrationAfterUpdate = bananaRegistrationToUpdate.calories
        Integer amountOfModifiedCalories = caloriesOfBananaRegistrationBeforeUpdate - caloriesOfBananaRegistrationAfterUpdate
        Integer finalTotalCalories = loggedUser.eatenCalories
        Integer remainingCaloriesAfterDelete = loggedUser.calculateRemainingCalories()

        //THEN
        Assertions.assertEquals(caloriesOfAppleRegistrationWhichStays + caloriesOfBananaRegistrationAfterUpdate, finalTotalCalories)
        Assertions.assertEquals((initialCaloriesEaten - finalTotalCalories).abs(), amountOfModifiedCalories.abs())
        Assertions.assertEquals((remainingCaloriesAfterDelete - remainingCaloriesBeforeDelete).abs(), amountOfModifiedCalories.abs())
        Assertions.assertEquals(loggedUser.objectiveCalories - remainingCaloriesAfterDelete, finalTotalCalories)
    }


}
