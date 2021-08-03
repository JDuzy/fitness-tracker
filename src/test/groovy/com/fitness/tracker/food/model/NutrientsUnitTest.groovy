package com.fitness.tracker.food.model

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@CompileStatic
class NutrientsUnitTest {

    @Test
    void bothNutrientsHaveSameMainNutrientCarbohydrates(){
        //Arrange
        Nutrients nutrients1 = new Nutrients( 21.0,  10.0, 10.0)
        Nutrients nutrients2 = new Nutrients( 19.0,  11.0, 5.0)

        //Act
        Boolean haveSameNutrient = nutrients1.hasSimilarNutrientDistributionTo(nutrients2)

        //Assert
        Assertions.assertEquals(true, haveSameNutrient, "Both Nutrients have same main nutrient which is Carbohydrate")

    }

    @Test
    void bothNutrientsHaveSameMainNutrientProtein(){
        //Arrange
        Nutrients nutrients1 = new Nutrients( 10.0,  21.0, 10.0)
        Nutrients nutrients2 = new Nutrients( 8.0,  19.0, 5.0)

        //Act
        Boolean haveSameNutrient = nutrients1.hasSimilarNutrientDistributionTo(nutrients2)

        //Assert
        Assertions.assertEquals(true, haveSameNutrient, "Both Nutrients have same main nutrient which is Protein")

    }

    @Test
    void bothNutrientsHaveSameMainNutrientFats(){
        //Arrange
        Nutrients nutrients1 = new Nutrients( 10.0, 10.0, 22.0)
        Nutrients nutrients2 = new Nutrients( 8.0, 4.0, 19.0)

        //Act
        Boolean haveSameNutrient = nutrients1.hasSimilarNutrientDistributionTo(nutrients2)

        //Assert
        Assertions.assertEquals(true, haveSameNutrient, "Both Nutrients have same main nutrient which is Protein")

    }

    @Test
    void bothNutrientsHaveNoMainNutrientFats(){
        //Arrange
        Nutrients nutrients1 = new Nutrients( 10.0, 12.0, 9.0)
        Nutrients nutrients2 = new Nutrients( 6.0, 4.0, 5.0)

        //Act
        Boolean haveSameNutrient = nutrients1.hasSimilarNutrientDistributionTo(nutrients2)

        //Assert
        Assertions.assertEquals(true, haveSameNutrient, "Both Nutrients have same main nutrient which is Protein")

    }

    @Test
    void bothNutrientsHaveDifferentNutrientDistribution(){
        //Arrange
        Nutrients nutrients1 = new Nutrients( 25.0,  0.4, 0.3) //Main nutrient Carbohydrate
        Nutrients nutrients2 = new Nutrients( 0.8,  18.5, 4.2) //Main nutrient Carbohydrate

        //Act
        Boolean haveSameNutrient = nutrients1.hasSimilarNutrientDistributionTo(nutrients2)

        //Assert
        Assertions.assertEquals(false, haveSameNutrient, "Both Nutrients have same main nutrient which is Protein")

    }


}
