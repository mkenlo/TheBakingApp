package com.mkenlo.baking.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mkenlo.baking.db.model.Steps;

import java.util.List;

@Dao
public interface StepsDao {

    @Insert
    void insertAll(List<Steps> step);

    @Query("SELECT * FROM Steps")
    List<Steps> getAllSteps();

    @Query("SELECT * FROM Steps WHERE id=:position and recipeId=:recipeId")
    Steps getStepByPosition(int position, int recipeId);

    @Query("SELECT * FROM Steps WHERE recipeId=:recipeId")
    List<Steps> getStepsByRecipeId(final int recipeId);
}
