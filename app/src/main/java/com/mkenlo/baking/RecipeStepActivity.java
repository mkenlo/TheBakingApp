package com.mkenlo.baking;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mkenlo.baking.model.DataUtils;
import com.mkenlo.baking.model.Recipe;
import com.mkenlo.baking.model.RecipeSteps;

public class RecipeStepActivity extends AppCompatActivity  implements RecipeStepFragment.OnFragmentInteractionListener{


    public static String ARG_STEP_ITEM = "recipe_step_item";
    public static String ARG_RECIPE_ID = "recipe_id";
    public static String ARG_FRAG_TRANSACTION_ADD = "add";
    public static String ARG_FRAG_TRANSACTION_REPLACE = "replace";
    public static boolean isUnitTest = false;


    private Recipe mRecipe;
    private boolean mIsLastStep = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        int recipeID = getIntent().getIntExtra(ARG_RECIPE_ID, 1);

        RecipeSteps stepItem;

        if(savedInstanceState!=null){
            recipeID = savedInstanceState.getInt(ARG_RECIPE_ID);
            stepItem = savedInstanceState.getParcelable(ARG_STEP_ITEM);
            setupFragmentUI(stepItem, ARG_FRAG_TRANSACTION_REPLACE);
        }
        else{

            stepItem =  getIntent().getParcelableExtra(ARG_STEP_ITEM);
            setupFragmentUI(stepItem, ARG_FRAG_TRANSACTION_ADD);

        }
        mRecipe = new DataUtils(this).getData().get(recipeID-1);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Rename UI title
            actionBar.setTitle(mRecipe.getName());

        }

    }


    private void setupFragmentUI(RecipeSteps item, String transactionMode){

        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_STEP_ITEM, item);
        arguments.putBoolean("last_step_item", mIsLastStep);

        RecipeStepFragment fragment = new RecipeStepFragment();
        fragment.setArguments(arguments);

        if(transactionMode.equalsIgnoreCase(ARG_FRAG_TRANSACTION_ADD)){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag_recipe_step_container, fragment)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_recipe_step_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onButtonNextStepClicked(int nextPosition) {
        if((int)nextPosition < mRecipe.getSteps().size()){
            RecipeSteps nextStep = mRecipe.getSteps().get((int)nextPosition);
            setupFragmentUI(nextStep, ARG_FRAG_TRANSACTION_REPLACE);
        }
        else{
            mIsLastStep = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_RECIPE_ID, mRecipe.getID());
        outState.putParcelable(ARG_STEP_ITEM, getIntent().getParcelableExtra(ARG_STEP_ITEM));
        super.onSaveInstanceState(outState);
    }
}
