package com.fitness.tracker.person.service

import com.fitness.tracker.exercise.model.Exercise
import com.fitness.tracker.exercise.model.ExerciseRegistration
import com.fitness.tracker.exercise.repository.ExerciseRegistrationRepository
import com.fitness.tracker.exercise.repository.ExerciseRepository
import com.fitness.tracker.person.model.Credentials
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.repository.CredentialsRepository
import com.fitness.tracker.person.repository.PersonRepository
import com.fitness.tracker.weight.model.WeightRegistration
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.server.ResponseStatusException

import javax.transaction.Transactional
import java.time.LocalDate

import static org.springframework.http.HttpStatus.NOT_FOUND


@Service
@CompileStatic
class PersonService implements UserDetailsService{

    @Autowired
    final PersonRepository personRepository

    @Autowired
    final CredentialsRepository credentialsRepository

    @Autowired
    final BCryptPasswordEncoder bCryptPasswordEncoder

    @Autowired
    final ExerciseRepository exerciseRepository

    @Autowired
    final ExerciseRegistrationRepository exerciseRegistrationRepository

    final static String PERSON_NOT_FOUND_MSG = "user with email %s not found"


    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        findPersonByEmail(email).orElseThrow( { new UsernameNotFoundException(String.format(PERSON_NOT_FOUND_MSG, email)) })
    }

    boolean emailUsedExists(Person person){
        credentialsRepository.findCredentialsByEmail(person.email).isPresent()
    }

    @Transactional
    Person save(Person person){
        System.out.println("IS ENTERINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG")
        personRepository.save(person)
    }

    Person getPrincipal(){
        Person person = null
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Person){
            person = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        }
        person
    }

    @Transactional
    Person register(Person person){
        if (emailUsedExists(person)){
            throw new IllegalStateException("Email already taken")
        }
        String encodedPassword = bCryptPasswordEncoder.encode(person.password)
        person.password = encodedPassword
        person.setNutritionalObjective()
        save(person)
    }

    Person update(Person person, Map<String, String> payload){
        person.sex = payload.get("sex")
        person.dateOfBirth = LocalDate.parse(payload.get("dateOfBirth"))
        person.height = payload.get("height").toInteger()
        person.weight = payload.get("weight").toBigDecimal()
        person.weightChangePerWeek = payload.get("objective").toBigDecimal()
        person.physicalActivity = payload.get("physicalActivity").toBigDecimal()
        person.setNutritionalObjective()
        save(person)
    }

    @Transactional
    Optional<Person> findPersonByEmail(String email){
        Optional<Credentials> credentials = credentialsRepository.findCredentialsByEmail(email)
        Optional<Person> person = Optional.empty()
        credentials.ifPresent({ person = personRepository.findPersonByCredentials(credentials.get())})
        person
    }


    void wasRegisteredValidly(Person person, BindingResult bindingResult) {
        if (emailUsedExists(person)){
            bindingResult.addError(new FieldError("user", "credentials.email", "Email adress already in use"))
        }

        if (!person.passwordsMatch()){
            bindingResult.addError(new FieldError("user", "credentials.rpassword", "Passwords must match"))
        }
    }


    List<ExerciseRegistration> getExercisesRegistrationsByDate(LocalDate date) {
        Person person = getPrincipal()
        person.getExercisesRegistrationsByDate(date)
    }

    List<WeightRegistration> getWeightRegistrationsByDate(LocalDate date) {
        Person person = getPrincipal()
        person.getWeightRegistrationsByDate(date)
    }

    @Transactional
    void registerExercise(LocalDate registrationDate, BigDecimal time, BigDecimal weight, Long exerciseId) {
        Person person = getPrincipal()
        Exercise exercise = exerciseRepository.findExerciseById(exerciseId).orElseThrow({
            new IllegalStateException("Exercise with id ${exerciseId} does not exists")
        })
        ExerciseRegistration registration = new ExerciseRegistration(person: person, registrationDate: registrationDate, time: time, weight: weight, exercise: exercise)
        person.addExerciseRegistration(registration)
        exerciseRegistrationRepository.save(registration)
    }

    @Transactional
    void updateExerciseRegistration(Long registrationId, BigDecimal newTime, BigDecimal newWeight) {
        Person person = getPrincipal()
        println person.findExerciseRegistrationWithId(registrationId)
        ExerciseRegistration registration = Optional.ofNullable(person.findExerciseRegistrationWithId(registrationId) as ExerciseRegistration)
                .orElseThrow({
                    new ResponseStatusException(NOT_FOUND, "No exerciseRegistration with id: ${registrationId} was found")
                })
        person.deleteExerciseRegistration(registration)
        registration.time = newTime
        registration.weight = newWeight
        person.addExerciseRegistration(registration)
        exerciseRegistrationRepository.save(registration)
        registration
    }

    @Transactional
    void deleteExerciseRegistration(Long registrationId) {
        Person person = getPrincipal()
        ExerciseRegistration registration = Optional.ofNullable(person.findExerciseRegistrationWithId(registrationId) as ExerciseRegistration)
                .orElseThrow({
                    new ResponseStatusException(NOT_FOUND, "No exerciseRegistration with id: ${registrationId} was found")
                })
        person.deleteExerciseRegistration(registration)
        exerciseRegistrationRepository.deleteById(registrationId)
    }

    void registerWeight(LocalDate registrationDate, BigDecimal weight) {
        Person person = getPrincipal()
        WeightRegistration registration = new WeightRegistration(person: person, registrationDate: registrationDate, weight: weight)
        person.addWeightRegistration(registration)
    }

    @Transactional
    void updateWeightRegistration(Long registrationId, BigDecimal newWeight) {
        Person person = getPrincipal()
        WeightRegistration registration = Optional.ofNullable(person.findWeightRegistrationWithId(registrationId) as WeightRegistration)
                .orElseThrow({
                    new ResponseStatusException(NOT_FOUND, "No weightRegistration with id: ${registrationId} was found")
                })
        person.deleteWeightRegistration(registration)
        registration.weight = newWeight
        person.addWeightRegistration(registration)
        registration
    }

    @Transactional
    void deleteWeightRegistration(Long registrationId) {
        Person person = getPrincipal()
        WeightRegistration registration = Optional.ofNullable(person.findWeightRegistrationWithId(registrationId) as WeightRegistration)
                .orElseThrow({
                    new ResponseStatusException(NOT_FOUND, "No weightRegistration with id: ${registrationId} was found")
                })
        person.deleteWeightRegistration(registration)
    }
}
