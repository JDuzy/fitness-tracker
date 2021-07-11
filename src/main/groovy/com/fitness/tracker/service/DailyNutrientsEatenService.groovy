package com.fitness.tracker.service

import com.fitness.tracker.model.DailyNutrientsEaten
import com.fitness.tracker.model.Nutrients
import com.fitness.tracker.model.Person
import com.fitness.tracker.repository.DailyNutrientsEatenRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.transaction.Transactional
import java.time.LocalDate

@Service
@CompileStatic
class DailyNutrientsEatenService {

    @Autowired
    final DailyNutrientsEatenRepository dailyNutrientsEatenRepository


    @Transactional
    void updateActualNutrientsEatenByEatenDayAndPerson(LocalDate eatenDay, Person person){
        Optional<DailyNutrientsEaten> dailyNutrientsEatenOptional = dailyNutrientsEatenRepository.findByEatenDayAndPersonId(eatenDay, person.id)
        DailyNutrientsEaten dailyNutrientsEatenForCertainDate = dailyNutrientsEatenOptional.orElse(new DailyNutrientsEaten(nutrients: new Nutrients(carbohydrates: 0, proteins: 0, fats: 0),eatenDay: eatenDay, person: person))
        person.actualDailyNutrientsEaten = dailyNutrientsEatenForCertainDate
    }

    void save(DailyNutrientsEaten dailyNutrientsEaten) {
        dailyNutrientsEatenRepository.save(dailyNutrientsEaten)
    }

}
