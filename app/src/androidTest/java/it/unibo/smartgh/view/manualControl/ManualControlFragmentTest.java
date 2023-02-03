package it.unibo.smartgh.view.manualControl;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.allOf;

import android.view.View;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.awaitility.Duration;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.view.AbstractActivityTest;

/**
 * Test to verify the correct behaviour of the manual control fragment.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class ManualControlFragmentTest extends AbstractActivityTest {

    @Override
    public void init() {
        super.init();
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.manual_control_button), withText("Imposta controllo manuale"),
                        isDisplayed()));
        materialButton.perform(click());
    }

    @Test
    public void parameterTest() {
        this.plant.getParameters().forEach((type, parameter) -> {
            await().pollInterval(Duration.TWO_HUNDRED_MILLISECONDS).atMost(Duration.FIVE_SECONDS).untilAsserted(() -> {
                ViewInteraction textView = onView(
                        allOf(withId(R.id.parameterName), withText(type.getTitle()),
                                withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                                isDisplayed()));
                textView.check(matches(withText(type.getTitle())));

                ViewInteraction textView3 = onView(
                        allOf(withId(R.id.optimalRange),
                                withText(parameter.getMin() + " - " + parameter.getMax() + parameter.getUnit()),
                                withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                                isDisplayed()));
                textView3.check(matches(withText(parameter.getMin() + " - " + parameter.getMax() + parameter.getUnit())));
            });
        });
    }

    @Test
    public void modalityTest() {
        await().pollInterval(Duration.TWO_HUNDRED_MILLISECONDS).atMost(Duration.FIVE_SECONDS).untilAsserted(() -> {
            ViewInteraction switch_ = onView(
                allOf(withId(R.id.manualControlChoice), withText("Controllo manuale:"),
                        withParent(withParent(withId(R.id.fragment_container_view))),
                        isDisplayed()));
            switch_.check(matches(isDisplayed()));
            this.viewModel.updateModality(Modality.MANUAL);
            switch_.check(matches(isChecked()));
            this.viewModel.updateModality(Modality.AUTOMATIC);
            switch_.check(matches(isNotChecked()));
        });
    }
}