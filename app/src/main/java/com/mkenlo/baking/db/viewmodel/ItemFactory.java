package com.mkenlo.baking.db.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ItemFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final Application mApplication;
    private final int mRecipeId;

    public ItemFactory(@NonNull Application application, int recipeId) {
        mApplication = application;
        mRecipeId = recipeId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ItemViewModel(mApplication, mRecipeId);
    }
}
