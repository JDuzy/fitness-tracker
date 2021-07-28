package com.fitness.tracker.food.controller

import com.fitness.tracker.food.model.FoodRegistration
import com.fitness.tracker.food.repository.FoodRegistrationRepository
import com.fitness.tracker.person.model.Credentials
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.repository.CredentialsRepository
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
import org.springframework.transaction.annotation.Transactional

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

    @Autowired
    FoodRegistrationRepository foodRegistrationRepository

    @Autowired
    BCryptPasswordEncoder passwordEncoder

    @Autowired
    CredentialsRepository credentialsRepository

    Person personUsedToTest

    Credentials credentialsUsedForTest

    @BeforeEach
    void setUp(){

        personRepository.deleteAll()

        //Set up the Person
        String password = passwordEncoder.encode("123456")
        LocalDate dob = LocalDate.now().minusYears(18)
        personUsedToTest = new Person( dateOfBirth: dob, weight: 80, height: 180, sex: "male", physicalActivity: 1.725, weightChangePerWeek: 150)
        personUsedToTest.setNutritionalObjective()
        credentialsUsedForTest = new Credentials(person: personUsedToTest, userName: "testUser", email: "testUser@mail.com", password: password, rpassword: password)

        credentialsRepository.save(credentialsUsedForTest)

        doReturn(personRepository.findById(personUsedToTest.id).orElseThrow({new IllegalStateException("Error seting up Person on testSetUp")})).when(personService).getLoggedPerson()
        //when(personService.getPrincipal()).thenReturn(person)
    }

    @Test
    @Transactional //TODO: Transactional on tests? Otherwise -> Lazy initialization error
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
                .with(SecurityMockMvcRequestPostProcessors.user(credentialsUsedForTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"1\", \"amount\": \"100\" }")
        )

        mockMvc.perform(MockMvcRequestBuilders.post(("/food/registration?registrationDate=${today.toString()}"))
                .with(SecurityMockMvcRequestPostProcessors.user(credentialsUsedForTest))
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
    @Transactional
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
                .with(SecurityMockMvcRequestPostProcessors.user(credentialsUsedForTest))
        )

        Integer remainingCaloriesAfterDelete = personUsedToTest.remainingCaloriesFor(today)
        Integer eatenCaloriesAfterDelete = personUsedToTest.eatenCaloriesFor(today)


        //THEN
        Assertions.assertEquals(initialCaloriesEaten - caloriesToDelete, eatenCaloriesAfterDelete)
        Assertions.assertEquals(remainingCaloriesBeforeDelete  + caloriesToDelete, remainingCaloriesAfterDelete)
        Assertions.assertEquals(personUsedToTest.objectiveCalories - remainingCaloriesAfterDelete, eatenCaloriesAfterDelete)
    }

    @Test
    @Transactional
    void updateAFoodRegistrationFromToday(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        BigDecimal bananaOldAmountOfGrams = 100
        BigDecimal bananaNewAmountOfGrams = 100
        BigDecimal appleAmountOfGrams = 80
        LocalDate today = LocalDate.now()

        FoodRegistration bananaRegistrationToUpdate = personService.registerFood(LocalDate.now(), bananaOldAmountOfGrams, 1) //100g of banana
        FoodRegistration appleRegistrationWhichStays = personService.registerFood(LocalDate.now(), appleAmountOfGrams, 2) //80g of apple

        Integer initialCaloriesEaten = personUsedToTest.eatenCaloriesFor(today)
        Integer caloriesOfBananaRegistrationBeforeUpdate = bananaRegistrationToUpdate.calories
        Integer caloriesOfAppleRegistrationWhichStays = appleRegistrationWhichStays.calories
        Integer remainingCaloriesBeforeDelete = personUsedToTest.remainingCaloriesFor(today)

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.put(("/food/registration/${bananaRegistrationToUpdate.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(credentialsUsedForTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"1\", \"amount\": \"${bananaNewAmountOfGrams}\" }")
        )

        Integer caloriesOfBananaRegistrationAfterUpdate =  foodRegistrationRepository.findById(bananaRegistrationToUpdate.id).orElseThrow({
            new IllegalStateException("FoodRegistration with id: ${bananaRegistrationToUpdate.id}")
        }).calories
        Integer amountOfModifiedCalories = caloriesOfBananaRegistrationBeforeUpdate - caloriesOfBananaRegistrationAfterUpdate

        //THEN
        Assertions.assertEquals(caloriesOfAppleRegistrationWhichStays + caloriesOfBananaRegistrationAfterUpdate, personUsedToTest.eatenCaloriesFor(today))
        Assertions.assertEquals((initialCaloriesEaten - personUsedToTest.eatenCaloriesFor(today)).abs(), amountOfModifiedCalories.abs())
        Assertions.assertEquals((personUsedToTest.remainingCaloriesFor(today) - remainingCaloriesBeforeDelete).abs(), amountOfModifiedCalories.abs())
        Assertions.assertEquals(personUsedToTest.objectiveCalories - personUsedToTest.eatenCaloriesFor(today), personUsedToTest.remainingCaloriesFor(today))
    }

    @Test
    @Transactional
    void registerAFoodYesterdayAndAFoodTodayWithoutPreviousFoodRegistrations(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        Integer bananaCaloriesPer100g = 84
        Integer appleCaloriesPer80g = 72
        LocalDate today = LocalDate.now()
        LocalDate yesterday = LocalDate.now().minusDays(1)
        Integer remainingCaloriesBeforeRegistrationYesterday = personUsedToTest.remainingCaloriesFor(yesterday)
        Integer remainingCaloriesBeforeRegistrationToday = personUsedToTest.remainingCaloriesFor(today)
        Integer objectiveCaloriesBefore = personUsedToTest.objectiveCalories

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post(("/food/registration?registrationDate=${LocalDate.now().minusDays(1).toString()}"))
                .with(SecurityMockMvcRequestPostProcessors.user(credentialsUsedForTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"1\", \"amount\": \"100\" }")
        )

        //THEN
        Assertions.assertEquals(bananaCaloriesPer100g, personUsedToTest.eatenCaloriesFor(yesterday))
        Assertions.assertEquals(remainingCaloriesBeforeRegistrationYesterday - bananaCaloriesPer100g, personUsedToTest.remainingCaloriesFor(yesterday))
        Assertions.assertEquals(objectiveCaloriesBefore, personUsedToTest.objectiveCalories)

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post(("/food/registration?registrationDate=${LocalDate.now().toString()}"))
                .with(SecurityMockMvcRequestPostProcessors.user(credentialsUsedForTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"foodId\": \"2\", \"amount\": \"80\" }")
        )

        //THEN
        Assertions.assertEquals(appleCaloriesPer80g, personUsedToTest.eatenCaloriesFor(today))
        Assertions.assertEquals(remainingCaloriesBeforeRegistrationToday - appleCaloriesPer80g, personUsedToTest.remainingCaloriesFor(today))
        Assertions.assertEquals(objectiveCaloriesBefore, personUsedToTest.objectiveCalories)
    }

    @Test
    @Transactional
    void deleteAFoodRegistrationFromTodayAndOneFromYesterdayLeavingPersonWithoutFoodRegistrations(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        //Today
        LocalDate today = LocalDate.now()
        FoodRegistration bananaRegistrationToDeleteToday = personService.registerFood(today, 100.0, 1) //100g of banana
        Integer remainingCaloriesForTodayBeforeDelete = personUsedToTest.remainingCaloriesFor(today)
        Integer eatenCaloriesTodayBeforeDelete = personUsedToTest.eatenCaloriesFor(today)

        //Yesterday
        LocalDate yesterday = LocalDate.now().minusDays(1)
        FoodRegistration appleRegistrationToDeleteYesterday = personService.registerFood(yesterday, 80.0, 2) //80g of apple
        Integer remainingCaloriesForYesterdayBeforeDelete = personUsedToTest.remainingCaloriesFor(yesterday)
        Integer eatenCaloriesYesterdayBeforeDelete = personUsedToTest.eatenCaloriesFor(yesterday)

        Integer objectiveCalories = personUsedToTest.objectiveCalories
        Integer caloriesFromBanana = bananaRegistrationToDeleteToday.calories
        Integer caloriesFromApple = appleRegistrationToDeleteYesterday.calories

        foodRegistrationRepository.save(appleRegistrationToDeleteYesterday) //TODO: Same here, if not id would be null

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete(("/food/registration/${bananaRegistrationToDeleteToday.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(credentialsUsedForTest))
        )

        //THEN
        Assertions.assertEquals(objectiveCalories, personUsedToTest.objectiveCalories)
        Assertions.assertEquals(eatenCaloriesTodayBeforeDelete - caloriesFromBanana, personUsedToTest.eatenCaloriesFor(today))
        Assertions.assertEquals(objectiveCalories - personUsedToTest.eatenCaloriesFor(today), personUsedToTest.remainingCaloriesFor(today))
        Assertions.assertEquals(remainingCaloriesForTodayBeforeDelete + caloriesFromBanana, personUsedToTest.remainingCaloriesFor(today))

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete(("/food/registration/${appleRegistrationToDeleteYesterday.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(credentialsUsedForTest))
        )

        //THEN
        Assertions.assertEquals(objectiveCalories, personUsedToTest.objectiveCalories)
        Assertions.assertEquals(eatenCaloriesYesterdayBeforeDelete - caloriesFromApple, personUsedToTest.eatenCaloriesFor(yesterday))
        Assertions.assertEquals(objectiveCalories - personUsedToTest.eatenCaloriesFor(yesterday), personUsedToTest.remainingCaloriesFor(yesterday))
        Assertions.assertEquals(remainingCaloriesForYesterdayBeforeDelete + caloriesFromApple, personUsedToTest.remainingCaloriesFor(yesterday))

    }

    @Test
    @Transactional
     void updateAFoodRegistrationFromTodayAndOneFromYesterday(){

        /*GIVEN: food with id: 1 -> 'Banana' with 84kcal per 100g
        food with id: 2 -> 'Apple' with 72kcal per 80g
        */
        //Today
        LocalDate today = LocalDate.now()
        FoodRegistration bananaRegistrationToUpdateToday = personService.registerFood(today, 100.0, 1) //100g of banana
        Integer remainingCaloriesForTodayBeforeUpdate = personUsedToTest.remainingCaloriesFor(today)
        Integer eatenCaloriesTodayBeforeUpdate = personUsedToTest.eatenCaloriesFor(today)

        //Yesterday
        LocalDate yesterday = LocalDate.now().minusDays(1)
        FoodRegistration appleRegistrationToUpdateYesterday = personService.registerFood(yesterday, 80.0, 2) //80g of apple
        Integer remainingCaloriesForYesterdayBeforeUpdate = personUsedToTest.remainingCaloriesFor(yesterday)
        Integer eatenCaloriesYesterdayBeforeUpdate = personUsedToTest.eatenCaloriesFor(yesterday)

        Integer objectiveCalories = personUsedToTest.objectiveCalories

        foodRegistrationRepository.save(appleRegistrationToUpdateYesterday) //TODO: Why its needed?

        //WHEN the person updates the amount of banana from 100g to 50g
        mockMvc.perform(MockMvcRequestBuilders.put(("/food/registration/${bananaRegistrationToUpdateToday.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(credentialsUsedForTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": \"50\" }")
        )

        FoodRegistration bananaFoodRegistrationAfterUpdate = foodRegistrationRepository.findById(bananaRegistrationToUpdateToday.id).orElseThrow({
            new IllegalStateException("FoodRegistration with id: ${bananaRegistrationToUpdateToday.id}")
        })

        //THEN
        Assertions.assertEquals(objectiveCalories, personUsedToTest.objectiveCalories)
        Assertions.assertEquals(bananaFoodRegistrationAfterUpdate.calories, personUsedToTest.eatenCaloriesFor(today))
        Assertions.assertEquals(objectiveCalories - personUsedToTest.eatenCaloriesFor(today), personUsedToTest.remainingCaloriesFor(today))
        Assertions.assertEquals((eatenCaloriesTodayBeforeUpdate - personUsedToTest.eatenCaloriesFor(today)).abs(), (remainingCaloriesForTodayBeforeUpdate - personUsedToTest.remainingCaloriesFor(today)).abs())

        //WHEN the person updates the amount of apple from 80g to 160g
        mockMvc.perform(MockMvcRequestBuilders.put(("/food/registration/${appleRegistrationToUpdateYesterday.id}"))
                .with(SecurityMockMvcRequestPostProcessors.user(credentialsUsedForTest))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": \"160\" }")
        )

        FoodRegistration appleFoodRegistrationAfterUpdate = foodRegistrationRepository.findById(appleRegistrationToUpdateYesterday.id).orElseThrow({
            new IllegalStateException("FoodRegistration with id: ${appleRegistrationToUpdateYesterday.id}")
        })

        //THEN
        Assertions.assertEquals(objectiveCalories, personUsedToTest.objectiveCalories)
        Assertions.assertEquals(appleFoodRegistrationAfterUpdate.calories, personUsedToTest.eatenCaloriesFor(yesterday))
        Assertions.assertEquals(objectiveCalories - personUsedToTest.eatenCaloriesFor(yesterday), personUsedToTest.remainingCaloriesFor(yesterday))
        Assertions.assertEquals((eatenCaloriesYesterdayBeforeUpdate - personUsedToTest.eatenCaloriesFor(yesterday)).abs(), (remainingCaloriesForYesterdayBeforeUpdate - personUsedToTest.remainingCaloriesFor(yesterday)).abs())
    }


}
