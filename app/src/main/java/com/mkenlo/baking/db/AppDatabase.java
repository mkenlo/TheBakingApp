package com.mkenlo.baking.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mkenlo.baking.db.dao.IngredientDao;
import com.mkenlo.baking.db.dao.RecipeDao;
import com.mkenlo.baking.db.dao.StepsDao;
import com.mkenlo.baking.db.model.DataBuilder;
import com.mkenlo.baking.db.model.Ingredient;
import com.mkenlo.baking.db.model.Recipe;
import com.mkenlo.baking.db.model.Steps;
import com.mkenlo.baking.network.FetchClient;
import com.mkenlo.baking.network.FetchService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

@Database(entities = {Recipe.class, Ingredient.class, Steps.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    public abstract RecipeDao recipeDao();

    public abstract IngredientDao ingredientDao();

    public abstract StepsDao stepsDao();

    private static final String DATABASE_NAME = "recipe_database";
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();
    private static AppDatabase INSTANCE;


    public static AppDatabase getInstance(final Context context, AppExecutors executors) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context.getApplicationContext(), executors);
                    INSTANCE.updateDatabaseCreated(context);
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        AppDatabase database = AppDatabase.getInstance(appContext, executors);
                        buildData(database, executors);

                    }
                }).build();
    }

    /* */

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    private static void insertData(final AppDatabase database, final List<Recipe> recipes,
                                   final List<Ingredient> ingredients, final List<Steps> directions) {

        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                database.recipeDao().insertAll(recipes);
                database.ingredientDao().insertAll(ingredients);
                database.stepsDao().insertAll(directions);
            }
        });
    }


    private static void buildData(final AppDatabase database, final AppExecutors executors) {

        /*Create handle for the RetrofitInstance interface*/
        FetchService service = FetchClient.getRetrofitInstance().create(FetchService.class);
        Call<List<Recipe>> call = service.listRecipes();

        call.enqueue(new retrofit2.Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                final List<Recipe> recipes = response.body();
                final List<Ingredient> ingredients = DataBuilder.extractIngredientData(response.body());
                final List<Steps> directions = DataBuilder.extractStepData(response.body());
                executors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        // Generate the data for pre-population
                        insertData(database, recipes, ingredients, directions);
                    }

                });
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d("Callback RETROFIT", t.getMessage());

            }
        });
    }


}
