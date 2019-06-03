package com.zaf.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zaf.bakingapp.models.Cake;
import com.zaf.bakingapp.models.Ingredients;
import com.zaf.bakingapp.models.Steps;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class DetailsActivityIntentTest {

    // Test the intent in DetailsActivity

    @Rule
    public final ActivityTestRule<DetailsScreenActivity> activityTestRule = new ActivityTestRule<DetailsScreenActivity>(DetailsScreenActivity.class){

        @Override
        protected Intent getActivityIntent() {
            Ingredients mockIngredient = new Ingredients("2", "CUP", "Graham Cracker crumbs");
            ArrayList<Ingredients> mockIngredientList = new ArrayList<>();
            mockIngredientList.add(mockIngredient);

            Steps mockSteps = new Steps(0, "Recipe Introduction", "Recipe Introduction", "", "");
            ArrayList<Steps> mockStepList = new ArrayList<>();
            mockStepList.add(mockSteps);

            Cake mockCake = new Cake("Nutella Pie", 0, mockIngredientList, mockStepList);


            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, DetailsScreenActivity.class);
            result.putExtra("Cake", mockCake);
            return result;
        }
    };

    @Test
    public void DetailsActivityLaunchIntentTest() {
        onView(ViewMatchers.withId(R.id.step_descrpition_short)).check(matches(withText("Recipe Introduction")));
        onView(ViewMatchers.withId(R.id.ingredient_quantity)).check(matches(withText("2")));
        onView(ViewMatchers.withId(R.id.ingredient_measure)).check(matches(withText("CUP")));
        onView(ViewMatchers.withId(R.id.ingredient_type)).check(matches(withText("Graham Cracker crumbs")));

    }

}