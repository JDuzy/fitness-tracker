package com.fitness.tracker.food.model


import com.fitness.tracker.person.model.Registration
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull
import java.time.LocalDate

@Entity
@Table(name = "food_registration")
@ToString
@CompileStatic
class FoodRegistration extends Registration{

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    Food food

    @NotNull
    BigDecimal amountOfGrams

    Nutrients getNutrients(){
        food.getNutrientsPerAmount(amountOfGrams)
    }

    Integer getCalories(){
        food.getCaloriesPerAmount(amountOfGrams)
    }

    Nutrients calculateNutrientsIfAmountWere(BigDecimal amount) {
        food.getNutrientsPerAmount(amount)
    }
}
