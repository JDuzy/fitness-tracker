package com.fitness.tracker.person.model

import com.fitness.tracker.exercise.model.ExerciseRegistration
import com.fitness.tracker.food.model.DailyNutrientsEaten
import com.fitness.tracker.food.model.DailyNutritionalObjective
import com.fitness.tracker.food.model.FoodRegistration
import com.fitness.tracker.food.model.Nutrients
import com.fitness.tracker.weight.model.WeightRegistration
import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past
import java.time.LocalDate
import java.time.Period

@Entity
@Table(name = "person")
@CompileStatic
@ToString
class Person implements UserDetails{

    @Id
    @SequenceGenerator(name = 'person_sequence', sequenceName = 'person_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_sequence")
    @Column( name = "id", updatable = false, nullable = false)
    Long id

    @NotNull(message = "Please enter a date of birth")
    @Past(message = "Please enter a valid date of birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth

    @NotBlank(message = "Enter your sex")
    String sex

    @NotNull(message = "Please enter a weight")
    BigDecimal weight

    @NotNull(message = "Please enter a height")
    Integer height

    @NotNull
    BigDecimal physicalActivity

    @NotNull
    BigDecimal weightChangePerWeek

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Valid
    Credentials credentials = new Credentials()

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    DailyNutritionalObjective nutritionalObjective = new DailyNutritionalObjective()

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "daily_nutrients_eaten_id", referencedColumnName = "id")
    DailyNutrientsEaten actualDailyNutrientsEaten = new DailyNutrientsEaten(nutrients: new Nutrients(carbohydrates: 0, proteins: 0, fats: 0), eatenDay: LocalDate.now(), person: this)

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    Set<ExerciseRegistration> exerciseRegistrations = new HashSet<>()

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    Set<WeightRegistration> weightRegistrations = new HashSet<>()

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER")
        Collections.singletonList(authority)
    }

    @Override
    String getPassword() {
        return credentials.password
    }

    void setPassword(String newPassword){
        credentials.password = newPassword
    }

    @Override
    String getUsername() {
        return credentials.email
    }

    String getEmail() {
        return credentials.email
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }

    Integer getAge(){
        Period.between(this.dateOfBirth, LocalDate.now()).getYears()
    }

    Boolean passwordsMatch() {
        credentials.passwordsMatch()
    }

    void setNutritionalObjective(){
        nutritionalObjective.calculateObjective(age, weight, height, sex, physicalActivity, weightChangePerWeek)
    }

    Nutrients remainingNutrientsForTheActualDay(){
        nutritionalObjective.calculateRemainingNutrients(actualDailyNutrientsEaten)
    }

    Integer remainingCaloriesForTheActualDay(){
        nutritionalObjective.calculateRemainingCalories(actualDailyNutrientsEaten)
    }

    Nutrients getObjectiveNutrients(){
        nutritionalObjective.objectiveNutrients
    }

    Integer getObjectiveCalories(){
        nutritionalObjective.objectiveCalories
    }

    Integer getEatenCaloriesOnActualDay(){
        actualDailyNutrientsEaten.calories
    }

    DailyNutrientsEaten addFoodRegistration(FoodRegistration foodRegistration) {
        actualDailyNutrientsEaten.addNutrientsBasedOn(foodRegistration)
        actualDailyNutrientsEaten
    }

    DailyNutrientsEaten deleteFoodRegistration(FoodRegistration foodRegistration) {
        actualDailyNutrientsEaten.deleteNutrientsBasedOn(foodRegistration)
        actualDailyNutrientsEaten
    }

    List<ExerciseRegistration> getExercisesRegistrationsByDate(LocalDate date){
        exerciseRegistrations.findAll {registration -> registration.wasRegisteredOn(date)}.toList()
    }

    List<WeightRegistration> getWeightRegistrationsByDate(LocalDate date) {
        weightRegistrations.findAll {registration -> registration.wasRegisteredOn(date) }.toList()
    }

    void addExerciseRegistration(ExerciseRegistration exerciseRegistration) {
        exerciseRegistrations.add(exerciseRegistration)
    }

    void deleteExerciseRegistration(ExerciseRegistration exerciseRegistration) {
        exerciseRegistrations.remove(exerciseRegistration)

    }

    ExerciseRegistration findExerciseRegistrationWithId(Long registrationId) {
        exerciseRegistrations.find({registration -> registration.id == registrationId})
    }

    void addWeightRegistration(WeightRegistration weightRegistration) {
        weightRegistrations.add(weightRegistration)
        this.weight = weightRegistration.weight
    }

    void findWeightRegistrationWithId(long registrationId) {
        weightRegistrations.find({registration -> registration.id == registrationId})
    }

    void deleteWeightRegistration(WeightRegistration weightRegistration) {
        weightRegistrations.remove(weightRegistration)
    }
}
