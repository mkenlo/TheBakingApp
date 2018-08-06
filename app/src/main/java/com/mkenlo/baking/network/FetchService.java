package com.mkenlo.baking.network;

import retrofit2.Call;
import retrofit2.http.GET;

import com.mkenlo.baking.db.model.Recipe;

import java.util.List;

public interface FetchService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> listRecipes();
}
