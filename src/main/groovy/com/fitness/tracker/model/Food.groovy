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
    Nutrients nutrientsPer100Gram

    @NotNull
    BigDecimal gramsInOnePortion

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    Characteristics characteristics

    Integer getCalories(){
        nutrientsPer100Gram.calories
    }

    BigDecimal getCarbohydrates(){
        nutrientsPer100Gram.carbohydrates
    }

    BigDecimal getProteins(){
        nutrientsPer100Gram.proteins
    }

    BigDecimal getFats(){
        nutrientsPer100Gram.fats
    }

    Nutrients getNutrientsPerAmount(BigDecimal amountOfGrams){
        new Nutrients(carbohydrates: (amountOfGrams * carbohydrates)/100 , proteins: (amountOfGrams * proteins)/100, fats: (amountOfGrams * fats)/100)
    }

    Integer getCaloriesPerAmount(BigDecimal amountOfGrams){
        getNutrientsPerAmount(amountOfGrams).calories
    }


}
