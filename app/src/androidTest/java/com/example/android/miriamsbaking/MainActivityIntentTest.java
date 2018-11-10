package com.example.android.miriamsbaking;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.miriamsbaking.activity.MainActivity;
import com.example.android.miriamsbaking.model.Recipe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.android.miriamsbaking.activity.MainActivity.RECIPE_EXTRA;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest{
@Rule
public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
        MainActivity.class);

@Before
public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        }

@Test
public void performClickOnRecyclerViewItem_checkThatIntentHasExtra() {
        // Find the first item in recyclerview and click on it
        onView(withId(R.id.rv_recipe))
        .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        intended(allOf(hasExtra(RECIPE_EXTRA
                ,new Recipe(0,"Nutella Pie"))));
        }
}
