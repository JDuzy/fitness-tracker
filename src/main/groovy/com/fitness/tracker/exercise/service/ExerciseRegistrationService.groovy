package com.fitness.tracker.exercise.service


import com.fitness.tracker.exercise.model.Exercise
import com.fitness.tracker.exercise.model.ExerciseRegistration
import com.fitness.tracker.exercise.repository.ExerciseRegistrationRepository
import com.fitness.tracker.exercise.repository.ExerciseRepository
import com.fitness.tracker.person.model.Person
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

import javax.transaction.Transactional
import java.time.LocalDate

import static org.springframework.http.HttpStatus.NOT_FOUND

@Service
@CompileStatic
class ExerciseRegistrationService {

    @Autowired
    final ExerciseRegistrationRepository exerciseRegistrationRepository

    @Autowired
    final ExerciseRepository exerciseRepository


    List<ExerciseRegistration> findAllExerciseRegistrationByPersonAndRegistrationDate(Person person, LocalDate registrationDate) {
        exerciseRegistrationRepository.findAllExerciseRegistrationByPersonAndRegistrationDate(person, registrationDate)
    }

    @Transactional
    ExerciseRegistration register(Person person, LocalDate registrationDate, BigDecimal time, Long exerciseId){
        Optional<Exercise> exercise = exerciseRepository.findExerciseById(exerciseId)
        exercise.orElseThrow({
            new IllegalStateException("Exercise with id ${exerciseId} does not exists")
        })
        ExerciseRegistration registration = new ExerciseRegistration(person: person, registrationDate: registrationDate, time: time, exercise: exercise.get())
        exerciseRegistrationRepository.save(registration)

    }

    @Transactional
    ExerciseRegistration update(long registrationId, BigDecimal newTime, Person person) {
        Optional<ExerciseRegistration> exerciseRegistration = exerciseRegistrationRepository.findById(registrationId)
        exerciseRegistration.orElseThrow({
            new ResponseStatusException(NOT_FOUND, "No exerciseRegistration with id: ${registrationId} was found")
        })
        ExerciseRegistration registration = exerciseRegistration.get()
        //person.deleteExerciseRegistration(registration)
        registration.time = newTime
        exerciseRegistrationRepository.save(registration)
    }

    @Transactional
    void deleteRegistrationById(long id, Person person) {
        Optional<ExerciseRegistration> registration =  exerciseRegistrationRepository.findExerciseRegistrationById(id)
        registration.orElseThrow({
          new ResponseStatusException(NOT_FOUND, "No exerciseRegistration with id: ${id} was found")
        })
        exerciseRegistrationRepository.deleteById(id)
    }
}
