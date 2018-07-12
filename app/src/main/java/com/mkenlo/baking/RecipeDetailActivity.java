package com.mkenlo.baking;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mkenlo.baking.model.DataUtils;
import com.mkenlo.baking.model.Ingredient;
import com.mkenlo.baking.model.Recipe;
import com.mkenlo.baking.model.RecipeSteps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepFragment.OnFragmentInteractionListener {


    public static String ARG_RECIPE_ID = "recipe id";
    @BindView(R.id.frag_recipe_step_container) FrameLayout mFragmentContainer;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        int recipeID = (int) getIntent().getLongExtra(ARG_RECIPE_ID, 0);
        Recipe recipe = new DataUtils(this).getData().get(recipeID);

        if(mFragmentContainer!=null){
            mTwoPane = true;
            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                Bundle arguments = new Bundle();
                arguments.putParcelable(RecipeStepFragment.ARG_STEP_ITEM,
                        recipe.getSteps().get(0));

                RecipeStepFragment fragment = new RecipeStepFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frag_recipe_step_container, fragment)
                        .commit();
            }

        }else{ mTwoPane = false;}




        // Rename UI title
        getSupportActionBar().setTitle(recipe.getName());

        ((TextView) findViewById(R.id.tv_recipe_name)).setText(recipe.getName());
        RecyclerView ingredientList = findViewById(R.id.rv_ingredient_list);
        ingredientList.setLayoutManager(new LinearLayoutManager(this));
        ingredientList.setAdapter(new IngredientListAdapter(recipe.getIngredients()));

        RecyclerView stepList = findViewById(R.id.rv_step_list);
        stepList.setLayoutManager(new LinearLayoutManager(this));
        stepList.setAdapter(new StepListAdapter(recipe.getSteps()));

    }


    public void onRecipeStepSelected(View view){

        RecipeSteps item = (RecipeSteps) view.getTag();
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(RecipeStepActivity.ARG_STEP_ITEM, item);
            RecipeStepFragment fragment = new RecipeStepFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_recipe_step_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtra(RecipeStepActivity.ARG_STEP_ITEM, item);
            startActivity(intent);
        }
    }



    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("FRAGMENT_INTERACTION", "Here is my callBack from Fragment");
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
            holder.mQuantity.setText(String.valueOf(mIngredients.get(position).getQuantity()));
            holder.mMeasure.setText(mIngredients.get(position).getMeasure());
            holder.mIngredientName.setText(mIngredients.get(position).getIngredient());

        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_ingredient_qty) TextView mQuantity;
            @BindView(R.id.tv_ingredient_measure) TextView mMeasure;
            @BindView(R.id.tv_ingredient_name) TextView mIngredientName;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


    public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ViewHolder> {

        private List<RecipeSteps> mSteps;
        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecipeStepSelected(v);
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

            holder.mStepId.setText(String.valueOf(mSteps.get(position).getID()));
            holder.mStepName.setText(mSteps.get(position).getShortDescription());

            holder.itemView.setTag(mSteps.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_step_id) TextView mStepId;
            @BindView(R.id.tv_step_name) TextView mStepName;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
