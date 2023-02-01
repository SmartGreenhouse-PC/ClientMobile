package it.unibo.smartgh.view.homepage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.entity.plant.PlantBuilder;
import it.unibo.smartgh.entity.plant.PlantParameter;
import it.unibo.smartgh.entity.plant.PlantParameterBuilder;
import it.unibo.smartgh.view.MainActivity;
import it.unibo.smartgh.viewmodel.GreenhouseViewModel;
import it.unibo.smartgh.viewmodel.GreenhouseViewModelImpl;

/**
 * Test to verify the correct behaviour of the homepage fragment.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    private MainActivity activity;
    private GreenhouseViewModel viewModel;
    private final Map<ParameterType, PlantParameter> parameters = new HashMap<ParameterType, PlantParameter>(){{
        put(ParameterType.TEMPERATURE, new PlantParameterBuilder("temperature").min(8.0).max(35.0).unit("\u2103").build());
        put(ParameterType.BRIGHTNESS, new PlantParameterBuilder("brightness").min(4200.0).max(130000.0).unit("Lux").build());
        put(ParameterType.SOIL_MOISTURE, new PlantParameterBuilder("soilMoisture").min(20.0).max(65.0).unit("%").build());
        put(ParameterType.HUMIDITY, new PlantParameterBuilder("humidity").min(30.0).max(80.0).unit("%").build());
    }};
    private final Plant plant =
        new PlantBuilder("Plant name").description("Plant description").image("http://is.am/5b4x").parameters(parameters).build();

    @Before
    public void init() {
        mActivityScenarioRule.getScenario().onActivity((activity) -> {
            this.activity = activity;
            this.viewModel = new ViewModelProvider(this.activity).get(GreenhouseViewModelImpl.class);
            this.viewModel.updatePlantInformation(this.plant);
        });
    }

    @Test
    public void basicInformationTest() {
        ViewInteraction textView = onView(
                allOf(withText("Pianta Coltivata"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.appcompat.widget.LinearLayoutCompat.class))),
                        isDisplayed()));
        textView.check(matches(withText("Pianta Coltivata")));

        ViewInteraction textView2 = onView(
                allOf(withText("Stato serra"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.appcompat.widget.LinearLayoutCompat.class))),
                        isDisplayed()));
        textView2.check(matches(withText("Stato serra")));

        ViewInteraction button = onView(
                allOf(withId(R.id.manual_control_button), withText("IMPOSTA CONTROLLO MANUALE"),
                        withParent(withParent(withId(R.id.fragment_container_view))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));
    }

    @Test
    public void basicParameterTest() {
        Arrays.stream(ParameterType.values()).map(ParameterType::getTitle).forEach(parameter -> {
            ViewInteraction textView = onView(
                    allOf(withId(R.id.homepage_parameter_name), withText(parameter),
                            withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.cardview.widget.CardView.class))),
                            isDisplayed()));
            textView.check(matches(withText(parameter)));
        });
    }

    @Test
    public void plantInformationTest() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.plant_name),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.appcompat.widget.LinearLayoutCompat.class))),
                        isDisplayed()));
        textView.check(matches(withText(plant.getName())));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.plant_image), withContentDescription("L'immagine della pianta coltivata"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.appcompat.widget.LinearLayoutCompat.class))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));
    }
}
