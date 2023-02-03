package it.unibo.smartgh.view.homepage;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import android.view.View;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Objects;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.parameter.ParameterValueImpl;
import it.unibo.smartgh.view.AbstractActivityTest;

/**
 * Test to verify the correct behaviour of the homepage parameter test.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeParameterTest extends AbstractActivityTest {

    @Test
    public void homeParameterTest() {
        this.plant.getParameters().forEach((type, parameter) -> {
            ViewInteraction textView = onView(
                    allOf(withId(R.id.homepage_parameter_name), withText(type.getTitle()),
                            withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.cardview.widget.CardView.class))),
                            isDisplayed()));
            textView.check(matches(withText(type.getTitle())));

            ViewInteraction textView2 = onView(
                    allOf(withId(R.id.homepage_parameter_optimal_value),
                            withText(parameter.getMin() + " - " + parameter.getMax() + " " + parameter.getUnit()),
                            withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.cardview.widget.CardView.class))),
                            isDisplayed()));
            textView2.check(matches(withText(containsString(parameter.getMin().toString()))));
        });
    }

    @Test
    public void currentValueTest() {
        final ParameterValue currentValue = new ParameterValueImpl("1", new Date(), 7.0);
        currentValue.setUnit(Objects.requireNonNull(plant.getParameters().get(ParameterType.TEMPERATURE)).getUnit());
        this.viewModel.updateParameterValue(ParameterType.TEMPERATURE, currentValue);
        ViewInteraction textView = onView(
                allOf(withId(R.id.homepage_parameter_current_value),
                        withText(currentValue.getValue() + " " + currentValue.getUnit()),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.cardview.widget.CardView.class))),
                        isDisplayed()));
        textView.check(matches(withText(currentValue.getValue() + " " + currentValue.getUnit())));
    }
}
