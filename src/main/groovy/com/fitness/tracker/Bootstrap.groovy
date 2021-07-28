package com.fitness.tracker

import com.fitness.tracker.food.model.Characteristics
import com.fitness.tracker.food.model.Food
import com.fitness.tracker.food.model.Nutrients
import com.fitness.tracker.food.repository.FoodRepository
import com.fitness.tracker.person.model.Credentials
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.repository.CredentialsRepository
import com.fitness.tracker.person.repository.PersonRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

import java.time.LocalDate


@Service
class Bootstrap implements InitializingBean {

    final Logger LOG = LoggerFactory.getLogger(Bootstrap.class)

    @Autowired
    FoodRepository foodRepository

    @Autowired
    PersonRepository userRepository

    @Autowired
    BCryptPasswordEncoder passwordEncoder

    @Autowired
    CredentialsRepository credentialsRepository


    @Override
    void afterPropertiesSet() throws Exception {
        LOG.info("Bootstrapping data")

        //Set up the Person
        String password = passwordEncoder.encode("123456")
        LocalDate dob = LocalDate.now().minusYears(18)
        Person person = new Person(dateOfBirth: dob, weight: 80, height: 180, sex: "Male", physicalActivity: 1.725, weightChangePerWeek: 150)
        person.setNutritionalObjective()

        Credentials credentials = new Credentials(person: person, userName: "user1", email: "mail@mail.com", password: password, rpassword: password)
        credentialsRepository.save(credentials)

        //Set up foods
        Characteristics characteristics = new Characteristics(isVegan: false, isPescetarian: false, isVegetarian: false)
        Food banana = new Food(name: "Banana", nutrientsPer100Gram: new Nutrients(carbohydrates: 20, proteins: 0.5, fats: 0.5), characteristics: characteristics, gramsInOnePortion: 100)
        Food apple = new Food(name: "Apple",  nutrientsPer100Gram: new Nutrients(carbohydrates: 20,proteins: 0.5, fats: 2), characteristics: characteristics, gramsInOnePortion: 80)
        Food pizza = new Food(name: "Pizza", nutrientsPer100Gram: new Nutrients(carbohydrates: 28,proteins: 5, fats: 9), characteristics: characteristics, gramsInOnePortion: 150)
        Food chickenBreast = new Food(name: "Chicken breast", nutrientsPer100Gram: new Nutrients(carbohydrates: 20,proteins: 22, fats: 5), characteristics: characteristics, gramsInOnePortion: 120)
        Food hamburguer = new Food(name: "Hamburguer",nutrientsPer100Gram: new Nutrients(carbohydrates: 38,proteins: 9, fats: 15), characteristics: characteristics, gramsInOnePortion: 300)
        foodRepository.saveAll(Arrays.asList(banana, apple, pizza, chickenBreast, hamburguer))

        LOG.info("Bootstrapping finished")
    }
}
