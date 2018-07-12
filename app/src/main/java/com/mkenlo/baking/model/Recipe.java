package com.mkenlo.baking.model;

import java.util.List;

public class Recipe {

    long   id;
    int    servings;
    String name;
    String image;
    List<Ingredient>   ingredients;
    List<RecipeSteps>   steps;


    public Recipe() {
    }

    public long getID() {
        return id;
    }

    public void setID(long ID) {
        this.id = ID;
    }

    public int getServing() {
        return servings;
    }

    public void setServing(int serving) {
        this.servings = serving;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeSteps> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeSteps> steps) {
        this.steps = steps;
    }
}
