package com.mkenlo.baking;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    public static int RECIPE_STEP_ID = 2;

    @Rule
    public ActivityTestRule<RecipeDetailActivity> detailActivityTestRule =
            new ActivityTestRule<>(RecipeDetailActivity.class);

    @Test
    public void onRecipeStepSelected() {
        onView(withId(R.id.rv_step_list)).perform(scrollTo()).perform(
                RecyclerViewActions.actionOnItemAtPosition(RECIPE_STEP_ID,click()));
        onView(withId(R.id.tv_step_id)).check(
                matches(withText("Step #"+String.valueOf(RECIPE_STEP_ID))));
    }


}