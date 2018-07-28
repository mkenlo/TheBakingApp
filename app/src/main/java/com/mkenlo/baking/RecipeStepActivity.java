package com.mkenlo.baking;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mkenlo.baking.model.DataUtils;
import com.mkenlo.baking.model.Recipe;
import com.mkenlo.baking.model.Steps;
import com.mkenlo.baking.utils.Constants;

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

        mRecipe = getIntent().getParcelableExtra(Constants.KEY_ITEM_RECIPE);

        Steps stepItem;

        if(savedInstanceState!=null){
            mRecipe = savedInstanceState.getParcelable(Constants.KEY_ITEM_RECIPE);
            stepItem = savedInstanceState.getParcelable(Constants.KEY_ITEM_STEP);
            setupFragmentUI(stepItem, Constants.KEY_FRAG_TRANSACTION_REPLACE);
        }
        else{

            stepItem =  getIntent().getParcelableExtra(Constants.KEY_ITEM_STEP);
            setupFragmentUI(stepItem, Constants.KEY_FRAG_TRANSACTION_ADD);

        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Rename UI title
            actionBar.setTitle(mRecipe.getName());

        }

    }


    private void setupFragmentUI(Steps item, String transactionMode){

        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.KEY_ITEM_STEP, item);
        arguments.putBoolean(Constants.KEY_ITEM_LAST_STEP, mIsLastStep);

        RecipeStepFragment fragment = new RecipeStepFragment();
        fragment.setArguments(arguments);

        if(transactionMode.equalsIgnoreCase(Constants.KEY_FRAG_TRANSACTION_ADD)){
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
        if(nextPosition < mRecipe.getSteps().size()){
            Steps nextStep = mRecipe.getSteps().get(nextPosition);
            setupFragmentUI(nextStep, Constants.KEY_FRAG_TRANSACTION_REPLACE);
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
        outState.putParcelable(Constants.KEY_ITEM_RECIPE, mRecipe);
        outState.putParcelable(Constants.KEY_ITEM_STEP, getIntent().getParcelableExtra(Constants.KEY_ITEM_STEP));
        super.onSaveInstanceState(outState);
    }
}
