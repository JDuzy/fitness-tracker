package com.fitness.tracker.repository

import com.fitness.tracker.model.DailyNutrientsEaten
import com.fitness.tracker.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.time.LocalDate

@Repository
interface DailyNutrientsEatenRepository extends JpaRepository< DailyNutrientsEaten, Long> {

    Optional<DailyNutrientsEaten> findByEatenDayAndPersonId(LocalDate eatenDay, Long personId)

    List<DailyNutrientsEaten> findAllByPerson(Person person)
}