package com.mkenlo.baking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mkenlo.baking.model.DataUtils;
import com.mkenlo.baking.model.Ingredient;
import com.mkenlo.baking.model.Recipe;
import com.mkenlo.baking.model.Steps;
import com.mkenlo.baking.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeStepFragment.OnFragmentInteractionListener {


    public static String ARG_RECIPE_ID = "recipe_id";
    private int recipeID;
    private Recipe mRecipe;
    private boolean mTwoPane;
    private boolean mIsLastStep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mIsLastStep = false;

        mRecipe = getIntent().getParcelableExtra(Constants.KEY_ITEM_RECIPE);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(Constants.KEY_ITEM_RECIPE);
        }


        if (findViewById(R.id.frag_recipe_step_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.

                Bundle arguments = new Bundle();
                arguments.putParcelable(Constants.KEY_ITEM_STEP, mRecipe.getSteps().get(0));
                arguments.putBoolean(Constants.KEY_ITEM_LAST_STEP, mIsLastStep);

                RecipeStepFragment fragment = new RecipeStepFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frag_recipe_step_container, fragment)
                        .commit();
            }

        } else {
            mTwoPane = false;
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Rename UI title
            actionBar.setTitle(mRecipe.getName());
        }
        mRecipe.getSteps();
        mRecipe.getIngredients();
        ((TextView) findViewById(R.id.tv_recipe_name)).setText(mRecipe.getName());
        RecyclerView ingredientList = findViewById(R.id.rv_ingredient_list);
        ingredientList.setLayoutManager(new LinearLayoutManager(this));
        ingredientList.setAdapter(new IngredientListAdapter(mRecipe.getIngredients()));

        RecyclerView stepList = findViewById(R.id.rv_step_list);
        stepList.setLayoutManager(new LinearLayoutManager(this));
        stepList.setAdapter(new StepListAdapter(mRecipe.getSteps()));

    }

    private void setupFragmentUI(Steps item) {

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.KEY_ITEM_RECIPE, mRecipe);
            arguments.putParcelable(Constants.KEY_ITEM_STEP, item);

            RecipeStepFragment fragment = new RecipeStepFragment();
            fragment.setArguments(arguments);
            arguments.putBoolean(Constants.KEY_ITEM_LAST_STEP, mIsLastStep);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_recipe_step_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtra(Constants.KEY_ITEM_STEP, item);
            intent.putExtra(Constants.KEY_ITEM_RECIPE, mRecipe);
            startActivity(intent);
        }
    }

    public void onRecipeStepSelected(View view) {

        Steps item = (Steps) view.getTag();
        setupFragmentUI(item);

    }

    @Override
    public void onButtonNextStepClicked(int position) {
        if (position < mRecipe.getSteps().size()) {
            Steps nextStep = mRecipe.getSteps().get(position);
            setupFragmentUI(nextStep);
        } else mIsLastStep = true;

    }


    public class IngredientListAdapter
            extends RecyclerView.Adapter<IngredientListAdapter.ViewHolder> {

        List<Ingredient> mIngredients;

        public IngredientListAdapter(List values) {
            this.mIngredients = values;
        }

        @NonNull
        @Override
        public IngredientListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                   int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_item_list, parent, false);
            return new IngredientListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IngredientListAdapter.ViewHolder holder,
                                     int position) {
            Ingredient ingredient = mIngredients.get(position);
            holder.mIngredientName.setText(ingredient.toString());

        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_ingredient)
            TextView mIngredientName;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


    public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ViewHolder> {

        private List<Steps> mSteps;
        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecipeStepSelected(v);
                v.setBackgroundResource(R.color.colorPrimaryLight);
            }
        };

        public StepListAdapter(List values) {
            super();
            this.mSteps = values;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_item_list, parent, false);
            return new StepListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.mStepName.setText(mSteps.get(position).toString());
            holder.itemView.setTag(mSteps.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);

        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_step_name)
            TextView mStepName;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.KEY_ITEM_RECIPE, mRecipe);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipe = savedInstanceState.getParcelable(Constants.KEY_ITEM_RECIPE);
    }
}
