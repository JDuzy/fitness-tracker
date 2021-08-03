package com.fitness.tracker

import com.fitness.tracker.exercise.model.Exercise
import com.fitness.tracker.exercise.repository.ExerciseRepository

import com.fitness.tracker.food.model.Food
import com.fitness.tracker.food.model.Nutrients
import com.fitness.tracker.food.repository.FoodRepository
import com.fitness.tracker.person.model.PhysicalObjective
import com.fitness.tracker.security.Credentials
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.security.repository.CredentialsRepository
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

import java.time.LocalDate


@Service
@CompileStatic
class Bootstrap implements InitializingBean {

    final Logger LOG = LoggerFactory.getLogger(Bootstrap.class)

    @Autowired
    FoodRepository foodRepository

    @Autowired
    ExerciseRepository exerciseRepository

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
        Person person = new Person(dateOfBirth: dob, weight: 80, height: 180, sex: "Male", physicalActivity: 1.725, physicalObjective: new PhysicalObjective( 150.0))
        person.setNutritionalObjective()

        Credentials credentials = new Credentials(person: person, userName: "user1", email: "mail@mail.com", password: password, rpassword: password)
        credentialsRepository.save(credentials)

        //Set up foods
        Food banana = new Food(name: "Banana", nutrientsPer100Gram: new Nutrients( 20.0, 0.5, 0.5), gramsInOnePortion: 100)
        Food apple = new Food(name: "Apple",  nutrientsPer100Gram: new Nutrients( 20.0, 0.5, 2.0), gramsInOnePortion: 80)
        Food pizza = new Food(name: "Pizza", nutrientsPer100Gram: new Nutrients( 28.0, 5.0, 9.0), gramsInOnePortion: 150)
        Food chickenBreast = new Food(name: "Chicken breast", nutrientsPer100Gram: new Nutrients( 1.0, 22.0, 5.0), gramsInOnePortion: 120)
        Food hamburger = new Food(name: "Hamburger",nutrientsPer100Gram: new Nutrients( 19.0,  15.0, 16.0), gramsInOnePortion: 300)
        foodRepository.saveAll(Arrays.asList(banana, apple, pizza, chickenBreast, hamburger))

        //Set up exercises
        Exercise benchPress = new Exercise(name: "Bench Press", type: Exercise.Type.STRENGTH, caloriesBurnedPerMinute: 3.7)
        Exercise overHeadPress = new Exercise(name: "Over head press", type: Exercise.Type.STRENGTH, caloriesBurnedPerMinute: 3.7)
        Exercise running = new Exercise(name: "Running", type: Exercise.Type.AEROBIC, caloriesBurnedPerMinute: 5)
        Exercise ropeJumps = new Exercise(name: "Rope jumps", type: Exercise.Type.AEROBIC, caloriesBurnedPerMinute: 5.4)
        exerciseRepository.saveAll(Arrays.asList(benchPress, overHeadPress, running, ropeJumps))

        LOG.info("Bootstrapping finished")
    }
}
