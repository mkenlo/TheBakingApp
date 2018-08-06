package com.mkenlo.baking.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.mkenlo.baking.db.model.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {

    @Insert
    void insertAll(List<Ingredient> ingredient);

    @Query("SELECT * FROM Ingredient")
    List<Ingredient> getAllIngredients();

    @Query("SELECT * FROM Ingredient WHERE recipeId=:recipeId")
    List<Ingredient> getIngredientsByRecipeId(final int recipeId);
}
