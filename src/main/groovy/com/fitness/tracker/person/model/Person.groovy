package com.fitness.tracker.person.model

import com.fitness.tracker.exercise.model.ExerciseRegistration
import com.fitness.tracker.food.model.DailyNutrientsEaten
import com.fitness.tracker.food.model.DailyNutritionalObjective
import com.fitness.tracker.food.model.FoodRegistration
import com.fitness.tracker.food.model.Nutrients
import groovy.transform.CompileStatic
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.server.ResponseStatusException

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
import javax.persistence.OrderBy
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past
import java.time.LocalDate
import java.time.Period

import static org.springframework.http.HttpStatus.NOT_FOUND

@Entity
@Table(name = "person")
@CompileStatic
class Person{

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
    DailyNutritionalObjective nutritionalObjective = new DailyNutritionalObjective()

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    Set<DailyNutrientsEaten> dailyNutrientsEaten = []

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    Set<ExerciseRegistration> exerciseRegistrations = new HashSet<>()

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    @JoinColumn(name = "person_id")
    Set<FoodRegistration> foodRegistrations = new LinkedHashSet<>() //TODO: OrderBy or LinkedHashSet

    Integer getAge(){
        Period.between(this.dateOfBirth, LocalDate.now()).getYears()
    }

    void setNutritionalObjective(){
        nutritionalObjective.calculateObjective(age, weight, height, sex, physicalActivity, weightChangePerWeek)
    }

    DailyNutrientsEaten dailyNutrientsEatenOn(LocalDate date){
        Optional.ofNullable(dailyNutrientsEaten.find {it.wereEatenOn(date)}).orElseGet({
            dailyNutrientsEaten.add(new DailyNutrientsEaten(nutrients: new Nutrients(carbohydrates: 0, proteins: 0, fats: 0),eatenDay: date))
            dailyNutrientsEaten.find({it.wereEatenOn(date)}) //Intellij cant resolve but it works
        })
    }

    Nutrients remainingNutrientsForTheActualDay(LocalDate date){
        nutritionalObjective.calculateRemainingNutrients(dailyNutrientsEatenOn(date))
    }

    Integer remainingCaloriesFor(LocalDate date){
        nutritionalObjective.calculateRemainingCalories(dailyNutrientsEatenOn(date))
    }

    Nutrients getObjectiveNutrients(){
        nutritionalObjective.objectiveNutrients
    }

    Integer getObjectiveCalories(){
        nutritionalObjective.objectiveCalories
    }

    Integer eatenCaloriesFor(LocalDate date){
        dailyNutrientsEatenOn(date).calories
    }

    void addFoodRegistration(FoodRegistration foodRegistration, LocalDate date) {
        foodRegistrations.add(foodRegistration)
        dailyNutrientsEatenOn(date).addNutrientsBasedOn(foodRegistration)
    }

    Set<FoodRegistration> getFoodRegistrationsByDate(LocalDate date) {
        foodRegistrations.findAll {registration -> registration.wasRegisteredOn(date) }
    }

    void updateFoodRegistrationWithId(Long registrationId, BigDecimal newAmount) {
        FoodRegistration registration = Optional.ofNullable(foodRegistrations.find{it.id.equals(registrationId)})
                .orElseThrow({new ResponseStatusException(NOT_FOUND, "No foodRegistration with id: ${registrationId} was found")})
        dailyNutrientsEatenOn(registration.registrationDate).updateEatenNutrientsBasedOn(registration, newAmount)
        registration.setAmountOfGrams(newAmount)
    }

    void deleteFoodRegistrationWithId(Long registrationId) {
        FoodRegistration registration = Optional.ofNullable(foodRegistrations.find{it.id.equals(registrationId)})
                .orElseThrow({new ResponseStatusException(NOT_FOUND, "No foodRegistration with id: ${registrationId} was found")})
        dailyNutrientsEatenOn(registration.registrationDate).deleteNutrientsBasedOn(registration)
        foodRegistrations.remove(registration)
    }

    void updateData(String newSex, LocalDate dateOfBirth, Integer height, BigDecimal weight, BigDecimal weightChangePerWeek, BigDecimal physicalActivity) {
        this.sex = sex
        this.dateOfBirth = dateOfBirth
        this.height = height
        this.weight = weight
        this.weightChangePerWeek = weightChangePerWeek
        this.physicalActivity = physicalActivity
        setNutritionalObjective()
    }

    List<ExerciseRegistration> getExercisesRegistrationsByDate(LocalDate date){
        exerciseRegistrations.findAll {registration -> registration.wasRegisteredOn(date)}.toList()
    }

    void addExerciseRegistration(ExerciseRegistration exerciseRegistration) {
        exerciseRegistrations.add(exerciseRegistration)
    }

    void deleteExerciseRegistration(ExerciseRegistration exerciseRegistration) {
        exerciseRegistrations.remove(exerciseRegistration)
    }

    List<ExerciseRegistration> getExerciseRegistrationsByDate(LocalDate date) {
        exerciseRegistrations.findAll {registration -> registration.wasRegisteredOn(date) }.toList()
    }

    ExerciseRegistration findExerciseRegistrationWithId(Long registrationId) {
        exerciseRegistrations.find({registration -> registration.id == registrationId})
    }

}
