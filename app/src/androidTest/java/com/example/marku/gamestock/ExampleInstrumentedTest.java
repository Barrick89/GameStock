package com.example.marku.gamestock;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<ConsoleOverviewActivity> mActivityTestRule =
            new ActivityTestRule<>(ConsoleOverviewActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.marku.gamestock", appContext.getPackageName());
    }

    //Test if the console icons are correctly displayed
    @Test
    public void checkConsoleViewIsVisible_ConsoleOverviewActivity() {
        onView(withId(R.id.gridview)).check(matches(isDisplayed()));
    }

    //Test if the GameCatalogActivity is correctly displayed
    @Test
    public void checkActivityIsVisible_GameCatalogActivity() {
        Intents.init();
        onData(anything()).inAdapterView(withId(R.id.gridview)).atPosition(0).perform(click());
        intended(hasComponent(GameCatalogActivity.class.getName()));
        Intents.release();
    }

    //Test if the plus button is working
    @Test
    public void checkPlusButton_EditorActivity() {
        onData(anything()).inAdapterView(withId(R.id.gridview)).atPosition(0).perform(click());
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.button_game_count_plus)).perform(click());
        onView(withId(R.id.edit_game_count)).check(matches(withText(containsString("1"))));
    }


}
