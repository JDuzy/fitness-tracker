package com.fitness.tracker

import com.fitness.tracker.model.Characteristics
import com.fitness.tracker.model.Credentials
import com.fitness.tracker.model.Food
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
        Person user = new Person(credentials: credentials, dateOfBirth: LocalDate.now().minusYears(18), weight: 80, height: 180, sex: "male", physicalActivity: "ACTIVE", weightChangePerWeek: 0.25)
        userRepository.save(user)

        Characteristics characteristics = new Characteristics(isVegan: false, isPescetarian: false, isVegetarian: false);
        Food banana = new Food(name: "Banana", calories: 100, carbohydrates: 20,proteins: 1, fats: 0.5, characteristics: characteristics)
        Food manzana = new Food(name: "Manzana", calories: 80, carbohydrates: 15,proteins: 2, fats: 0.8, characteristics: characteristics)
        Food pizza = new Food(name: "Pizza", calories: 200, carbohydrates: 28,proteins: 5, fats: 9, characteristics: characteristics)
        Food pechugaDePollo = new Food(name: "Pechuga De Pollo", calories: 180, carbohydrates: 0.2,proteins: 22, fats: 5, characteristics: characteristics)
        Food hamburguesa = new Food(name: "Hamburguesa", calories: 350, carbohydrates: 38,proteins: 9, fats: 15, characteristics: characteristics)

        foodRepository.saveAll(Arrays.asList(banana, manzana, pizza, pechugaDePollo, hamburguesa))

        LOG.info("Bootstrapping finished")
    }
}
