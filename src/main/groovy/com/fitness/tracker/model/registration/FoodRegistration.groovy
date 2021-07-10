package com.fitness.tracker.model.registration

import com.fitness.tracker.model.Food
import com.fitness.tracker.model.Nutrients

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "food_registration")
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
}
