package com.fitness.tracker.model

import groovy.transform.CompileStatic

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
import javax.validation.constraints.NotNull


@Entity
@Table(name = "food")
@CompileStatic
class Food {

    @Id
    @SequenceGenerator(name = 'food_sequence', sequenceName = 'food_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_sequence")
    @Column( name = "id", updatable = false, nullable = false)
    long id

    @NotNull
    String name

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutrients_id", referencedColumnName = "id")
    Nutrients nutrientsPerGram

    @NotNull
    BigDecimal gramsInOnePortion

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    Characteristics characteristics

    Integer getCalories(){
        nutrientsPerGram.calories
    }

    BigDecimal getCarbohydrates(){
        nutrientsPerGram.carbohydrates
    }

    BigDecimal getProteins(){
        nutrientsPerGram.proteins
    }

    BigDecimal getFats(){
        nutrientsPerGram.fats
    }

    Nutrients getNutrientsPerAmount(BigDecimal amountOfGrams){
        new Nutrients(carbohydrates: amountOfGrams.multiply(carbohydrates) , proteins: amountOfGrams * proteins, fats: amountOfGrams * fats)
    }

    Integer getCaloriesPerAmount(BigDecimal amountOfGrams){
        getNutrientsPerAmount(amountOfGrams).calories
    }


}
