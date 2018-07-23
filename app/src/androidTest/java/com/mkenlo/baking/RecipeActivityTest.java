package com.mkenlo.baking;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.Espresso.onView;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    private static int RECIPE_ID = 0;
    private static String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<RecipeActivity> mRecipeActivityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);


   @Test
    public void clickRecipeItem_OpensDetailActivity() {
        onView(withId(R.id.rv_recipe_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(RECIPE_ID,click()));
        onView(withId(R.id.tv_recipe_name)).check(matches(withText(RECIPE_NAME)));
    }
}