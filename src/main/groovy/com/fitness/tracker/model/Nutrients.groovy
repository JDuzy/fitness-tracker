package com.fitness.tracker.model

import javax.validation.constraints.NotNull

class Nutrients {

    @NotNull
    BigDecimal carbohydrates

    @NotNull
    BigDecimal proteins

    @NotNull
    BigDecimal fats


    Integer getCalories(){
        BigDecimal calories = (carbohydrates * 4 + proteins * 4 + fats * 4).setScale(0, RoundingMode.HALF_UP);
        calories.toInteger()
    }

    Nutrients minus(Nutrients other){
        new Nutrients(carbohydrates: this.carbohydrates - other.carbohydrates, proteins: this.proteins - other.proteins, fats: this.fats - fats)
    }

    void update(BigDecimal carbohydrates, BigDecimal proteins, BigDecimal fats) {
        this.carbohydrates = carbohydrates
        this.proteins = proteins
        this.fats = fats
    }
}
