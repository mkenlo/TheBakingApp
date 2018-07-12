package com.mkenlo.baking;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class RecipeStepActivity extends AppCompatActivity  implements RecipeStepFragment.OnFragmentInteractionListener{


    public static String ARG_STEP_ITEM = "recipe step object";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);


        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepFragment.ARG_STEP_ITEM,
                getIntent().getParcelableExtra(ARG_STEP_ITEM));
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frag_recipe_step_container, fragment)
                .commit();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("FRAGMENT_INTERACTION", "Here is my callBAck from Fragment");
    }
}
