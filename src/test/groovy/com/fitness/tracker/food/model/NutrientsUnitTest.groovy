package com.fitness.tracker.food.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class NutrientsUnitTest {

    @Test
    public void bothNutrientsHaveSameMainNutrientCarbohydrates(){
        //Arrange
        Nutrients nutrients1 = new Nutrients(gramsOfCarbohydrates: 21, gramsOfProtein: 10, gramsOfFats: 10)
        Nutrients nutrients2 = new Nutrients(gramsOfCarbohydrates: 19, gramsOfProtein: 11, gramsOfFats: 5)

        //Act
        Boolean haveSameNutrient = nutrients1.hasSimilarNutrientDistributionTo(nutrients2)

        //Assert
        Assertions.assertEquals(true, haveSameNutrient, "Both Nutrients have same main nutrient which is Carbohydrate")

    }

    @Test
    public void bothNutrientsHaveSameMainNutrientProtein(){
        //Arrange
        Nutrients nutrients1 = new Nutrients(gramsOfCarbohydrates: 10, gramsOfProtein: 21, gramsOfFats: 10)
        Nutrients nutrients2 = new Nutrients(gramsOfCarbohydrates: 8, gramsOfProtein: 19, gramsOfFats: 5)

        //Act
        Boolean haveSameNutrient = nutrients1.hasSimilarNutrientDistributionTo(nutrients2)

        //Assert
        Assertions.assertEquals(true, haveSameNutrient, "Both Nutrients have same main nutrient which is Protein")

    }

    @Test
    public void bothNutrientsHaveSameMainNutrientFats(){
        //Arrange
        Nutrients nutrients1 = new Nutrients(gramsOfCarbohydrates: 10, gramsOfProtein: 10, gramsOfFats: 22)
        Nutrients nutrients2 = new Nutrients(gramsOfCarbohydrates: 8, gramsOfProtein: 4, gramsOfFats: 19)

        //Act
        Boolean haveSameNutrient = nutrients1.hasSimilarNutrientDistributionTo(nutrients2)

        //Assert
        Assertions.assertEquals(true, haveSameNutrient, "Both Nutrients have same main nutrient which is Protein")

    }

    @Test
    public void bothNutrientsHaveNoMainNutrientFats(){
        //Arrange
        Nutrients nutrients1 = new Nutrients(gramsOfCarbohydrates: 10, gramsOfProtein: 12, gramsOfFats: 9)
        Nutrients nutrients2 = new Nutrients(gramsOfCarbohydrates: 6, gramsOfProtein: 4, gramsOfFats: 5)

        //Act
        Boolean haveSameNutrient = nutrients1.hasSimilarNutrientDistributionTo(nutrients2)

        //Assert
        Assertions.assertEquals(true, haveSameNutrient, "Both Nutrients have same main nutrient which is Protein")

    }

    @Test
    public void bothNutrientsHaveDifferentNutrientDistribution(){
        //Arrange
        Nutrients nutrients1 = new Nutrients(gramsOfCarbohydrates: 25, gramsOfProtein: 0.4, gramsOfFats: 0.3) //Main nutrient Carbohydrate
        Nutrients nutrients2 = new Nutrients(gramsOfCarbohydrates: 0.8, gramsOfProtein: 18.5, gramsOfFats: 4.2) //Main nutrient Carbohydrate

        //Act
        Boolean haveSameNutrient = nutrients1.hasSimilarNutrientDistributionTo(nutrients2)

        //Assert
        Assertions.assertEquals(false, haveSameNutrient, "Both Nutrients have same main nutrient which is Protein")

    }


}
