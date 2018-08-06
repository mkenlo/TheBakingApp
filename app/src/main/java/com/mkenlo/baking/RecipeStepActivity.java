package com.mkenlo.baking;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mkenlo.baking.db.AppExecutors;
import com.mkenlo.baking.db.BasicApp;
import com.mkenlo.baking.db.DataRepository;
import com.mkenlo.baking.db.model.Recipe;
import com.mkenlo.baking.db.model.Steps;
import com.mkenlo.baking.utils.Constants;

public class RecipeStepActivity extends AppCompatActivity implements RecipeStepFragment.OnFragmentInteractionListener {


    private Recipe mRecipe;
    private boolean mIsLastStep = false;
    public static String STEP_FRAGMENT_TAG = "step_fragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        mRecipe = getIntent().getParcelableExtra(Constants.KEY_ITEM_RECIPE);

        Steps stepItem;

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(Constants.KEY_ITEM_RECIPE);
            stepItem = savedInstanceState.getParcelable(Constants.KEY_ITEM_STEP);
            RecipeStepFragment frag = (RecipeStepFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, STEP_FRAGMENT_TAG);
            setupFragmentUI(stepItem, Constants.KEY_FRAG_TRANSACTION_REPLACE, frag);
        } else {

            stepItem = getIntent().getParcelableExtra(Constants.KEY_ITEM_STEP);
            setupFragmentUI(stepItem, Constants.KEY_FRAG_TRANSACTION_ADD, null);

        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Rename UI title
            actionBar.setTitle(mRecipe.getName());

        }

    }


    private void setupFragmentUI(Steps item, String transactionMode, @Nullable RecipeStepFragment frag) {

        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.KEY_ITEM_STEP, item);
        arguments.putBoolean(Constants.KEY_ITEM_LAST_STEP, mIsLastStep);
        RecipeStepFragment fragment = new RecipeStepFragment();
        if(frag!= null)
            fragment = frag;

        fragment.setArguments(arguments);

        if (transactionMode.equalsIgnoreCase(Constants.KEY_FRAG_TRANSACTION_ADD)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag_recipe_step_container, fragment, STEP_FRAGMENT_TAG)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_recipe_step_container, fragment, STEP_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onButtonNextStepClicked(final int nextPosition) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                DataRepository repository = ((BasicApp) getApplication()).getRepository();
                Steps nextStep = repository.getStepByPosition(nextPosition, mRecipe.getID());
                if (nextStep != null){
                  getSupportFragmentManager().findFragmentByTag(STEP_FRAGMENT_TAG);
                    setupFragmentUI(nextStep,
                            Constants.KEY_FRAG_TRANSACTION_REPLACE,
                            (RecipeStepFragment) getSupportFragmentManager().findFragmentByTag(STEP_FRAGMENT_TAG));}
                else
                    mIsLastStep = true;
            }
        });

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
        Fragment frag = getSupportFragmentManager().findFragmentByTag(STEP_FRAGMENT_TAG);
        getSupportFragmentManager().putFragment(outState, STEP_FRAGMENT_TAG, frag);
        super.onSaveInstanceState(outState);
    }
}
