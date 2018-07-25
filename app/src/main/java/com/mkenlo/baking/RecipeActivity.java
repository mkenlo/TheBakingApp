package com.mkenlo.baking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkenlo.baking.model.DataUtils;
import com.mkenlo.baking.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {

    @BindView(R.id.rv_recipe_list) RecyclerView mRecipeListView;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ButterKnife.bind(this);

        mLayoutManager = new GridLayoutManager(this, calculateBestColumnCount());
        mRecipeListView.setLayoutManager(mLayoutManager);
        mRecipeListView.setAdapter(new RecipeListAdapter(new DataUtils(this).getData()));
    }

    public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder>{


        private List<Recipe> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Recipe item = (Recipe) view.getTag();
                Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailActivity.ARG_RECIPE_ID, item.getID() );
                startActivity(intent);
            }

        };

        public RecipeListAdapter(List<Recipe> mValues) {
            this.mValues = mValues;
        }

        @NonNull
        @Override
        public RecipeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_item_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeListAdapter.ViewHolder holder, int position) {
            holder.name.setText(mValues.get(position).getName());
            int dummyIcon = R.drawable.ic_muffin;
            if(!mValues.get(position).getImage().isEmpty())
                Picasso.get()
                        .load(mValues.get(position).getImage())
                        .placeholder(dummyIcon)
                        .into(holder.dummyIcon);


            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.tv_recipe_name) TextView name;
            @BindView(R.id.iv_dummy_icon)  ImageView dummyIcon;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private int calculateBestColumnCount() {
        int defaultSize = 600;
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / defaultSize);
    }

}
