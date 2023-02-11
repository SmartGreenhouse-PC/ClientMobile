package it.unibo.smartgh.view.selectGreenhouse;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.allOf;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.Espresso.onView;

import android.view.View;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unibo.smartgh.view.AbstractActivityTest;

/**
 * Test to verify the correct behaviour of the select greenhouse fragment.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SelectGreenhouseFragmentTest extends AbstractActivityTest {

    @Test
    public void basicInformationTest(){
        ViewInteraction textview = onView(allOf(withText("Seleziona una serra"),
                withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.appcompat.widget.LinearLayoutCompat.class))),
                isDisplayed()));
    }
}
