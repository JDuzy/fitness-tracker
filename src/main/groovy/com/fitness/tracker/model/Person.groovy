package com.fitness.tracker.model

import com.fitness.tracker.model.registration.FoodRegistration
import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
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

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutrients_id", referencedColumnName = "id")
    Nutrients dailyNutrientsEaten = new Nutrients(carbohydrates: 0, proteins: 0, fats: 0)

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

    Nutrients calculateRemainingNutrients(){
        nutritionalObjective.calculateRemainingNutrients(dailyNutrientsEaten)
    }

    BigDecimal calculateRemainingCalories(){
        nutritionalObjective.calculateRemainingCalories(dailyNutrientsEaten)
    }

    Nutrients getObjectiveNutrients(){
        nutritionalObjective.objectiveNutrients
    }

    BigDecimal getObjectiveCalories(){
        nutritionalObjective.objectiveCalories
    }

    void addFoodRegistration(FoodRegistration foodRegistration) {
        dailyNutrientsEaten.addNutrientsBasedOn(foodRegistration)
    }

    void deleteFoodRegistration(FoodRegistration foodRegistration) {
        dailyNutrientsEaten.deleteNutrientsBasedOn(foodRegistration)
    }

    void updateFoodRegistration(FoodRegistration foodRegistration, BigDecimal newAmount) {
        dailyNutrientsEaten.updateNutrientsBasedOn(foodRegistration, newAmount)
    }
}