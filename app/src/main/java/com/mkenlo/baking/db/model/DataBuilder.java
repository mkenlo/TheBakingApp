package com.mkenlo.baking.db.model;


import java.util.ArrayList;
import java.util.List;


public class DataBuilder {

    public static List<Ingredient> extractIngredientData(List<Recipe> list){
        List<Ingredient> allIngredients = new ArrayList<>();

        for(Recipe recipe:list){
            for(Ingredient ingredient:recipe.getIngredients()){
                ingredient.setRecipeId(recipe.getID());
                allIngredients.add(ingredient);
            }
        }
        return allIngredients;

    }

    public static List<Steps> extractStepData(List<Recipe> list){

        List<Steps> allSteps = new ArrayList<>();

        for(Recipe recipe:list){
            for(Steps direction:recipe.getSteps()){
                direction.setRecipeId(recipe.getID());
                allSteps.add(direction);
            }
        }
        return allSteps;
    }
}
