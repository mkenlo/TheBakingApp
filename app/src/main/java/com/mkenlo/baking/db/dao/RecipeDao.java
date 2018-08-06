package com.mkenlo.baking.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.mkenlo.baking.db.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM Recipe")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM Recipe")
    List<Recipe> getRecipes();

    @Query("SELECT * FROM Recipe WHERE id=:recipeId")
    Recipe getRecipeById(int recipeId);

    @Insert
    void insertAll(List<Recipe> recipe);

}
