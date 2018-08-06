package com.mkenlo.baking.db.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.mkenlo.baking.db.BasicApp;
import com.mkenlo.baking.db.DataRepository;
import com.mkenlo.baking.db.model.Ingredient;
import com.mkenlo.baking.db.model.Recipe;
import com.mkenlo.baking.db.model.Steps;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    DataRepository mRepository;

    public ItemViewModel(@NonNull Application application, int recipeId) {
        super(application);
        mRepository = ((BasicApp) application).getRepository();
    }

    public List<Ingredient> getIngredientsByRecipeId(int recipeId) {
        return mRepository.getIngredientsByRecipeId(recipeId);
    }

    public List<Steps> getStepsByRecipeId(int recipeId) {
        return mRepository.getStepsByRecipeId(recipeId);
    }

    public Recipe getRecipeById(int recipeId) {
        return mRepository.getRecipeById(recipeId);
    }
}
