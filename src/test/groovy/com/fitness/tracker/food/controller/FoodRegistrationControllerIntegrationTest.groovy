package com.fitness.tracker.food.controller

import com.fitness.tracker.food.model.FoodRegistration
import com.fitness.tracker.food.repository.DailyNutrientsEatenRepository
import com.fitness.tracker.food.repository.FoodRegistrationRepository
import com.fitness.tracker.person.model.Credentials
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.repository.PersonRepository
import com.fitness.tracker.person.service.PersonService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.http.MediaType

import java.time.LocalDate

import static org.mockito.Mockito.doReturn

@SpringBootTest
@AutoConfigureMockMvc
class FoodRegistrationControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc

    @SpyBean
    PersonService personService

    @Autowired
    PersonRepository personRepository

    Person personUsedToTest

    @Autowired
    FoodRegistrationRepository foodRegistrationRepository

    @Autowired
    BCryptPasswordEncoder passwordEncoder

    @Autowired
    DailyNutrientsEatenRepository dailyNutrientsEatenRepository


    @BeforeEach
    void setUp(){

        foodRegistrationRepository.deleteAll()
        dailyNutrientsEatenRepository.findAll().forEach({
            it.setPerson(null)
            dailyNutrientsEatenRepository.delete(it)
        })
        personRepository.deleteAll()

        //Set up credentials for the person
        String password = passwordEncoder.encode("123456")
        Credentials credentials = new Credentials(userName: "testUser", email: "testUser@mail.com", password: password, rpassword: password)

        //Set up the Person
        //DailyNutrientsEaten ints created on the attribute
        LocalDate dob = LocalDate.now().minusYears(18)
        Person person = new Person(credentials: credentials, dateOfBirth: dob, weight: 80, height: 180, sex: "male", physicalActivity: 1.725, weightChangePerWeek: 150)
        person.setNutritionalObjective()
        personUsedToTest = person
        personRepository.save(person)

        doReturn(personRepository.findById(person.id).orElseThrow({new IllegalStateException("Error seting up Person on testSetUp")})).when(personService).getPrincipal()
        //when(personService.getPrincipal()).thenReturn(person)
    }

    @Test
    void registerSomeFoodsForTodayWithoutPreviousFoodRegistrations(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        Integer bananaCaloriesPer100g = 84
        Integer appleCaloriesPer80g = 72
        LocalDate today = LocalDate.now()
        Integer remainingCaloriesBeforeRegistrations = personUsedToTest.remainingCaloriesFor(today)
        Integer objectiveCalories = personUsedToTest.objectiveCalories

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post(("/food/registration?registrationDate=${today.toString()}"))
                .with(SecurityMockMvcRequestPostProcessors.user(personUsedToTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"1\", \"amount\": \"100\" }")
        )

        mockMvc.perform(MockMvcRequestBuilders.post(("/food/registration?registrationDate=${today.toString()}"))
                .with(SecurityMockMvcRequestPostProcessors.user(personUsedToTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"2\", \"amount\": \"160\" }")
        )

        Integer remainingCaloriesAfterRegistrations = personUsedToTest.remainingCaloriesFor(today)
        Integer eatenCaloriesAfterRegistration = personUsedToTest.eatenCaloriesFor(today)

        //THEN
        Assertions.assertEquals(bananaCaloriesPer100g + appleCaloriesPer80g * 2, eatenCaloriesAfterRegistration)
        Assertions.assertEquals(remainingCaloriesBeforeRegistrations - remainingCaloriesAfterRegistrations, eatenCaloriesAfterRegistration)
        Assertions.assertEquals(objectiveCalories - remainingCaloriesAfterRegistrations, eatenCaloriesAfterRegistration)
    }

    @Test
    void deleteAFoodRegistrationFromTodayLeavingPersonWithoutFoodRegistrations(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        LocalDate today = LocalDate.now()
        FoodRegistration registrationToDelete = personService.registerFood(LocalDate.now(), 100.0, 1) //100g of banana
        personService.registerFood(LocalDate.now(), 80.0, 2) //80g of apple

        Integer initialCaloriesEaten = personUsedToTest.eatenCaloriesFor(today)
        Integer caloriesToDelete = registrationToDelete.calories
        Integer remainingCaloriesBeforeDelete = personUsedToTest.remainingCaloriesFor(today)

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete(("/food/registration/${registrationToDelete.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(personUsedToTest))
        )

        Integer remainingCaloriesAfterDelete = personUsedToTest.remainingCaloriesFor(today)
        Integer eatenCaloriesAfterDelete = personUsedToTest.eatenCaloriesFor(today)


        //THEN
        Assertions.assertEquals(initialCaloriesEaten - caloriesToDelete, eatenCaloriesAfterDelete)
        Assertions.assertEquals(remainingCaloriesBeforeDelete  + caloriesToDelete, remainingCaloriesAfterDelete)
        Assertions.assertEquals(personUsedToTest.objectiveCalories - remainingCaloriesAfterDelete, eatenCaloriesAfterDelete)
    }

    @Test
    void updateAFoodRegistrationFromToday(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        BigDecimal bananaOldAmountOfGrams = 100
        BigDecimal bananaNewAmountOfGrams = 100
        BigDecimal appleAmountOfGrams = 80

        FoodRegistration bananaRegistrationToUpdate = personService.registerFood(LocalDate.now(), bananaOldAmountOfGrams, 1) //100g of banana
        FoodRegistration appleRegistrationWhichStays = personService.registerFood(LocalDate.now(), appleAmountOfGrams, 2) //80g of apple

        Integer initialCaloriesEaten = personUsedToTest.eatenCaloriesOnActualDay
        Integer caloriesOfBananaRegistrationBeforeUpdate = bananaRegistrationToUpdate.calories
        Integer caloriesOfAppleRegistrationWhichStays = appleRegistrationWhichStays.calories
        Integer remainingCaloriesBeforeDelete = personUsedToTest.remainingCaloriesFor()

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.put(("/food/registration/${bananaRegistrationToUpdate.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(personUsedToTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"1\", \"amount\": \"${bananaNewAmountOfGrams}\" }")
        )

        Integer caloriesOfBananaRegistrationAfterUpdate =  foodRegistrationRepository.findById(bananaRegistrationToUpdate.id).orElseThrow({
            new IllegalStateException("FoodRegistration with id: ${bananaRegistrationToUpdate.id}")
        }).calories
        Integer amountOfModifiedCalories = caloriesOfBananaRegistrationBeforeUpdate - caloriesOfBananaRegistrationAfterUpdate

        //THEN
        Assertions.assertEquals(caloriesOfAppleRegistrationWhichStays + caloriesOfBananaRegistrationAfterUpdate, personUsedToTest.eatenCaloriesOnActualDay)
        Assertions.assertEquals((initialCaloriesEaten - personUsedToTest.eatenCaloriesOnActualDay).abs(), amountOfModifiedCalories.abs())
        Assertions.assertEquals((personUsedToTest.remainingCaloriesFor() - remainingCaloriesBeforeDelete).abs(), amountOfModifiedCalories.abs())
        Assertions.assertEquals(personUsedToTest.objectiveCalories - personUsedToTest.eatenCaloriesOnActualDay, personUsedToTest.remainingCaloriesFor())
    }

    @Test
    void registerAFoodYesterdayAndAFoodTodayWithoutPreviousFoodRegistrations(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        Integer bananaCaloriesPer100g = 84
        Integer appleCaloriesPer80g = 72
        Integer remainingCaloriesBeforeRegistrations = personUsedToTest.remainingCaloriesFor()
        Integer objectiveCaloriesBefore = personUsedToTest.objectiveCalories

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post(("/food/registration?registrationDate=${LocalDate.now().minusDays(1).toString()}"))
                .with(SecurityMockMvcRequestPostProcessors.user(personUsedToTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"1\", \"amount\": \"100\" }")
        )

        //THEN
        Assertions.assertEquals(bananaCaloriesPer100g, personUsedToTest.eatenCaloriesOnActualDay)
        Assertions.assertEquals(remainingCaloriesBeforeRegistrations - bananaCaloriesPer100g, personUsedToTest.remainingCaloriesFor())
        Assertions.assertEquals(objectiveCaloriesBefore, personUsedToTest.objectiveCalories)

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post(("/food/registration?registrationDate=${LocalDate.now().toString()}"))
                .with(SecurityMockMvcRequestPostProcessors.user(personUsedToTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"2\", \"amount\": \"80\" }")
        )

        //THEN
        Assertions.assertEquals(appleCaloriesPer80g, personUsedToTest.eatenCaloriesOnActualDay)
        Assertions.assertEquals(remainingCaloriesBeforeRegistrations - appleCaloriesPer80g, personUsedToTest.remainingCaloriesFor())
        Assertions.assertEquals(objectiveCaloriesBefore, personUsedToTest.objectiveCalories)
    }

    @Test
    void deleteAFoodRegistrationFromTodayAndOneFromYesterdayLeavingPersonWithoutFoodRegistrations(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        //Today
        FoodRegistration bananaRegistrationToDeleteToday = personService.registerFood(LocalDate.now(), 100.0, 1) //100g of banana
        Integer remainingCaloriesForTodayBeforeDelete = personUsedToTest.remainingCaloriesFor()
        Integer eatenCaloriesTodayBeforeDelete = personUsedToTest.eatenCaloriesOnActualDay

        //Yesterday
        FoodRegistration appleRegistrationToDeleteYesterday = personService.registerFood(LocalDate.now().minusDays(1), 80.0, 2) //80g of apple
        Integer remainingCaloriesForYesterdayBeforeDelete = personUsedToTest.remainingCaloriesFor()
        Integer eatenCaloriesYesterdayBeforeDelete = personUsedToTest.eatenCaloriesOnActualDay

        Integer objectiveCalories = personUsedToTest.objectiveCalories
        Integer caloriesFromBanana = bananaRegistrationToDeleteToday.calories
        Integer caloriesFromApple = appleRegistrationToDeleteYesterday.calories

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete(("/food/registration/${bananaRegistrationToDeleteToday.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(personUsedToTest))
        )

        //THEN
        Assertions.assertEquals(objectiveCalories, personUsedToTest.objectiveCalories)
        Assertions.assertEquals(eatenCaloriesTodayBeforeDelete - caloriesFromBanana, personUsedToTest.eatenCaloriesOnActualDay)
        Assertions.assertEquals(objectiveCalories - personUsedToTest.eatenCaloriesOnActualDay, personUsedToTest.remainingCaloriesFor())
        Assertions.assertEquals(remainingCaloriesForTodayBeforeDelete + caloriesFromBanana, personUsedToTest.remainingCaloriesFor())

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete(("/food/registration/${appleRegistrationToDeleteYesterday.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(personUsedToTest))
        )

        //THEN
        Assertions.assertEquals(objectiveCalories, personUsedToTest.objectiveCalories)
        Assertions.assertEquals(eatenCaloriesYesterdayBeforeDelete - caloriesFromApple, personUsedToTest.eatenCaloriesOnActualDay)
        Assertions.assertEquals(objectiveCalories - personUsedToTest.eatenCaloriesOnActualDay, personUsedToTest.remainingCaloriesFor())
        Assertions.assertEquals(remainingCaloriesForYesterdayBeforeDelete + caloriesFromApple, personUsedToTest.remainingCaloriesFor())

    }

    @Test
     void updateAFoodRegistrationFromTodayAndOneFromYesterday(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        FoodRegistration bananaRegistrationToUpdateToday = personService.registerFood(LocalDate.now(), 100.0, 1) //100g of banana
        Integer remainingCaloriesForTodayBeforeUpdate = personUsedToTest.remainingCaloriesFor()
        Integer eatenCaloriesTodayBeforeUpdate = personUsedToTest.eatenCaloriesOnActualDay

        FoodRegistration appleRegistrationToUpdateYesterday = personService.registerFood(LocalDate.now().minusDays(1), 80.0, 2) //80g of apple
        Integer remainingCaloriesForYesterdayBeforeUpdate = personUsedToTest.remainingCaloriesFor()
        Integer eatenCaloriesYesterdayBeforeUpdate = personUsedToTest.eatenCaloriesOnActualDay

        Integer objectiveCalories = personUsedToTest.objectiveCalories

        //WHEN the person updates the amount of banana from 100g to 50g
        mockMvc.perform(MockMvcRequestBuilders.put(("/food/registration/${bananaRegistrationToUpdateToday.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(personUsedToTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": \"50\" }")
        )

        FoodRegistration bananaFoodRegistrationAfterUpdate = foodRegistrationRepository.findById(bananaRegistrationToUpdateToday.id).orElseThrow({
            new IllegalStateException("FoodRegistration with id: ${bananaRegistrationToUpdateToday.id}")
        })

        //THEN
        Assertions.assertEquals(objectiveCalories, personUsedToTest.objectiveCalories)
        Assertions.assertEquals(bananaFoodRegistrationAfterUpdate.calories, personUsedToTest.eatenCaloriesOnActualDay)
        Assertions.assertEquals(objectiveCalories - personUsedToTest.eatenCaloriesOnActualDay, personUsedToTest.remainingCaloriesFor())
        Assertions.assertEquals((eatenCaloriesTodayBeforeUpdate - personUsedToTest.eatenCaloriesOnActualDay).abs(), (remainingCaloriesForTodayBeforeUpdate - personUsedToTest.remainingCaloriesFor()).abs())

        //WHEN the person updates the amount of apple from 80g to 160g
        mockMvc.perform(MockMvcRequestBuilders.put(("/food/registration/${appleRegistrationToUpdateYesterday.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(personUsedToTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": \"160\" }")
        )

        FoodRegistration appleFoodRegistrationAfterUpdate = foodRegistrationRepository.findById(appleRegistrationToUpdateYesterday.id).orElseThrow({
            new IllegalStateException("FoodRegistration with id: ${appleRegistrationToUpdateYesterday.id}")
        })

        //THEN
        Assertions.assertEquals(objectiveCalories, personUsedToTest.objectiveCalories)
        Assertions.assertEquals(appleFoodRegistrationAfterUpdate.calories, personUsedToTest.eatenCaloriesOnActualDay)
        Assertions.assertEquals(objectiveCalories - personUsedToTest.eatenCaloriesOnActualDay, personUsedToTest.remainingCaloriesFor())
        Assertions.assertEquals((eatenCaloriesYesterdayBeforeUpdate - personUsedToTest.eatenCaloriesOnActualDay).abs(), (remainingCaloriesForYesterdayBeforeUpdate - personUsedToTest.remainingCaloriesFor()).abs())
    }


}
