package it.unibo.smartgh.view;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.parameter.ParameterValueImpl;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.entity.plant.PlantBuilder;
import it.unibo.smartgh.entity.plant.PlantParameter;
import it.unibo.smartgh.entity.plant.PlantParameterBuilder;
import it.unibo.smartgh.viewmodel.GreenhouseViewModel;
import it.unibo.smartgh.viewmodel.GreenhouseViewModelImpl;

/**
 * Abstract class for testing main activity with plant and parameters configuration.
 */
public abstract class AbstractActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    protected MainActivity activity;
    protected GreenhouseViewModel greenhouseViewModel;
    protected ParameterValue currentValue;
    protected final Map<ParameterType, PlantParameter> parameters = new HashMap<ParameterType, PlantParameter>(){{
        put(ParameterType.TEMPERATURE, new PlantParameterBuilder("temperature").min(8.0).max(35.0).unit("\u2103").build());
        put(ParameterType.BRIGHTNESS, new PlantParameterBuilder("brightness").min(4200.0).max(130000.0).unit("Lux").build());
        put(ParameterType.SOIL_MOISTURE, new PlantParameterBuilder("soilMoisture").min(20.0).max(65.0).unit("%").build());
        put(ParameterType.HUMIDITY, new PlantParameterBuilder("humidity").min(30.0).max(80.0).unit("%").build());
    }};
    protected final Plant plant =
            new PlantBuilder("Plant name").description("Plant description").image("http://is.am/5b4x").parameters(parameters).build();

    @Before
    public void init() {
        mActivityScenarioRule.getScenario().onActivity((activity) -> {
            this.activity = activity;
            this.greenhouseViewModel = new ViewModelProvider(this.activity).get(GreenhouseViewModelImpl.class);
            this.greenhouseViewModel.updateGreenhousesName(List.of("greenhouse1"));
            this.greenhouseViewModel.updatePlantInformation(this.plant);
            this.currentValue = new ParameterValueImpl("greenhouse1", new Date(), 7.0);
            this.currentValue.setUnit(Objects.requireNonNull(plant.getParameters().get(ParameterType.TEMPERATURE)).getUnit());
            this.greenhouseViewModel.updateParameterValue(ParameterType.TEMPERATURE, currentValue);
        });
    }
}
