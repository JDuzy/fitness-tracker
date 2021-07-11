package com.fitness.tracker

import com.fitness.tracker.model.Characteristics
import com.fitness.tracker.model.Credentials
import com.fitness.tracker.model.DailyNutrientsEaten
import com.fitness.tracker.model.DailyNutritionalObjective
import com.fitness.tracker.model.Food
import com.fitness.tracker.model.Nutrients
import com.fitness.tracker.model.Person
import com.fitness.tracker.repository.FoodRepository
import com.fitness.tracker.repository.PersonRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service

import java.time.LocalDate


@Service
class Bootstrap implements InitializingBean {

    final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    FoodRepository foodRepository

    @Autowired
    PersonRepository userRepository

    @Autowired
    BCryptPasswordEncoder passwordEncoder


    @Override
    void afterPropertiesSet() throws Exception {
        LOG.info("Bootstrapping data")
        //Set up credentials for the person
        String password = passwordEncoder.encode("123456")
        Credentials credentials = new Credentials(userName: "user1", email: "mail@mail.com", password: password, rpassword: password);

        //Set up the Person
        //DailyNutrientsEaten ints created on the attribute
        LocalDate dob = LocalDate.now().minusYears(18)
        Person person = new Person(credentials: credentials, dateOfBirth: dob, weight: 80, height: 180, sex: "male", physicalActivity: 1.725, weightChangePerWeek: 150)
        person.setNutritionalObjective()
        userRepository.save(person)

        //Set up foods
        Characteristics characteristics = new Characteristics(isVegan: false, isPescetarian: false, isVegetarian: false);
        Food banana = new Food(name: "Banana", nutrientsPer100Gram: new Nutrients(carbohydrates: 20, proteins: 0.5, fats: 0.5), characteristics: characteristics, gramsInOnePortion: 100)
        Food manzana = new Food(name: "Apple",  nutrientsPer100Gram: new Nutrients(carbohydrates: 20,proteins: 0.5, fats: 2), characteristics: characteristics, gramsInOnePortion: 80)
        Food pizza = new Food(name: "Pizza", nutrientsPer100Gram: new Nutrients(carbohydrates: 28,proteins: 5, fats: 9), characteristics: characteristics, gramsInOnePortion: 150)
        Food pechugaDePollo = new Food(name: "Chicken breast", nutrientsPer100Gram: new Nutrients(carbohydrates: 20,proteins: 22, fats: 5), characteristics: characteristics, gramsInOnePortion: 120)
        Food hamburguesa = new Food(name: "Hamburguer",nutrientsPer100Gram: new Nutrients(carbohydrates: 38,proteins: 9, fats: 15), characteristics: characteristics, gramsInOnePortion: 300)
        foodRepository.saveAll(Arrays.asList(banana, manzana, pizza, pechugaDePollo, hamburguesa))

        LOG.info("Bootstrapping finished")
    }
}
