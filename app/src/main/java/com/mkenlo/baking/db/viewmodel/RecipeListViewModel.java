package com.mkenlo.baking.db.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.mkenlo.baking.db.BasicApp;
import com.mkenlo.baking.db.model.Recipe;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {


    private LiveData<List<Recipe>> mAllRecipes;

    public RecipeListViewModel (Application application) {
        super(application);
        mAllRecipes = ((BasicApp) application).getRepository().getAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes() { return mAllRecipes; }

}
