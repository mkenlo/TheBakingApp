package com.mkenlo.baking.db;

import android.arch.lifecycle.LiveData;

import com.mkenlo.baking.db.model.Ingredient;
import com.mkenlo.baking.db.model.Recipe;
import com.mkenlo.baking.db.model.Steps;

import java.util.List;

public class DataRepository {

    private final AppDatabase mDatabase;
    private static DataRepository sInstance;

    public DataRepository(final AppDatabase database) {
        mDatabase = database;

    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Recipe>> getAllRecipes() {
         return mDatabase.recipeDao().getAllRecipes();
    }

    public List<Recipe> getRecipes() {
        return mDatabase.recipeDao().getRecipes();
    }

    public Recipe getRecipeById(final int recipeId) {
        return mDatabase.recipeDao().getRecipeById(recipeId);
    }

    public List<Ingredient> getIngredientsByRecipeId(final int recipeId) {
        return mDatabase.ingredientDao().getIngredientsByRecipeId(recipeId);
    }

    public List<Steps> getStepsByRecipeId(final int recipeId) {
        return mDatabase.stepsDao().getStepsByRecipeId(recipeId); }

    public Steps getStepByPosition(final int position, final int recipeId) {
        return mDatabase.stepsDao().getStepByPosition(position, recipeId); }
}
