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

import com.mkenlo.baking.db.AppExecutors;
import com.mkenlo.baking.db.BasicApp;
import com.mkenlo.baking.db.DataRepository;
import com.mkenlo.baking.db.model.Ingredient;
import com.mkenlo.baking.db.model.Recipe;
import com.mkenlo.baking.db.model.Steps;
import com.mkenlo.baking.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeStepFragment.OnFragmentInteractionListener {


    private int mRecipeId;
    private Recipe mRecipe;
    private List<Steps> mStepsByRecipe;
    private List<Ingredient> mIngredientsByRecipe;

    private boolean mTwoPane;
    private boolean mIsLastStep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mIsLastStep = false;

        mRecipeId = getIntent().getIntExtra(Constants.KEY_ITEM_RECIPE_ID, 0);
        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(Constants.KEY_ITEM_RECIPE_ID);
        }

        populateUI();

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
        if (position < mStepsByRecipe.size()) {
            Steps nextStep = mStepsByRecipe.get(position);
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
        outState.putInt(Constants.KEY_ITEM_RECIPE_ID, mRecipe.getID());
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipeId = savedInstanceState.getInt(Constants.KEY_ITEM_RECIPE_ID);

    }

    private void setViews(){
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Rename UI title
            actionBar.setTitle(mRecipe.getName());
        }

        ((TextView) findViewById(R.id.tv_recipe_name)).setText(mRecipe.getName());
        RecyclerView ingredientList = findViewById(R.id.rv_ingredient_list);
        ingredientList.setLayoutManager(new LinearLayoutManager(this));
        ingredientList.setAdapter(new IngredientListAdapter(mIngredientsByRecipe));

        RecyclerView stepList = findViewById(R.id.rv_step_list);
        stepList.setLayoutManager(new LinearLayoutManager(this));
        stepList.setAdapter(new StepListAdapter(mStepsByRecipe));
    }

    private void initFragment(){
        if (findViewById(R.id.frag_recipe_step_container) != null) {
            mTwoPane = true;
        //    if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                Bundle arguments = new Bundle();
                arguments.putParcelable(Constants.KEY_ITEM_STEP, mStepsByRecipe.get(0));
                arguments.putBoolean(Constants.KEY_ITEM_LAST_STEP, mIsLastStep);

                RecipeStepFragment fragment = new RecipeStepFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frag_recipe_step_container, fragment)
                        .commit();
          //  }

        } else {
            mTwoPane = false;
        }
    }

    private void populateUI() {


        AppExecutors.getInstance().diskIO().execute(
                new Runnable() {
                    @Override
                    public void run() {

                        DataRepository repository = ((BasicApp) getApplication()).getRepository();
                        mRecipe = repository.getRecipeById(mRecipeId);
                        mStepsByRecipe = repository.getStepsByRecipeId(mRecipeId);
                        mIngredientsByRecipe = repository.getIngredientsByRecipeId(mRecipeId);

                        initFragment();
                        setViews();

                    }
                });
    }


}
