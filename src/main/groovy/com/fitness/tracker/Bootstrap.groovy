package com.fitness.tracker

import com.fitness.tracker.model.Characteristics
import com.fitness.tracker.model.Credentials
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

        String password = passwordEncoder.encode("123456")
        Credentials credentials = new Credentials(userName: "user1", email: "mail@mail.com", password: password, rpassword: password);

        Nutrients nutrientsEaten = new Nutrients(carbohydrates: 0, proteins: 0, fats: 0)
        Person person = new Person(credentials: credentials, dateOfBirth: LocalDate.now().minusYears(18), weight: 80, height: 180, sex: "male", physicalActivity: 1.725, weightChangePerWeek: 150, dailyNutrientsEaten: nutrientsEaten)
        person.setNutritionalObjective()
        userRepository.save(person)

        Characteristics characteristics = new Characteristics(isVegan: false, isPescetarian: false, isVegetarian: false);
        Food banana = new Food(name: "Banana", nutrientsPerGram: new Nutrients(carbohydrates: 0.2, proteins: 0.005, fats: 0.005), characteristics: characteristics, gramsInOnePortion: 100)
        Food manzana = new Food(name: "Manzana",  nutrientsPerGram: new Nutrients(carbohydrates: 0.2,proteins: 0.005, fats: 0.02), characteristics: characteristics, gramsInOnePortion: 80)
        Food pizza = new Food(name: "Pizza", nutrientsPerGram: new Nutrients(carbohydrates: 0.28,proteins: 0.05, fats: 0.09), characteristics: characteristics, gramsInOnePortion: 150)
        Food pechugaDePollo = new Food(name: "Pechuga De Pollo", nutrientsPerGram: new Nutrients(carbohydrates: 0.2,proteins: 0.22, fats: 0.05), characteristics: characteristics, gramsInOnePortion: 120)
        Food hamburguesa = new Food(name: "Hamburguesa",nutrientsPerGram: new Nutrients(carbohydrates: 0.38,proteins: 0.09, fats: 0.15), characteristics: characteristics, gramsInOnePortion: 300)

        foodRepository.saveAll(Arrays.asList(banana, manzana, pizza, pechugaDePollo, hamburguesa))

        LOG.info("Bootstrapping finished")
    }
}
