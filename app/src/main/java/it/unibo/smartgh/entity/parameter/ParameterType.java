package it.unibo.smartgh.entity.parameter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import it.unibo.smartgh.R;

/**
 * The enum that represents the possible types of Parameter.
 */
public enum ParameterType {

    /**
     * The brightness parameter.
     */
    BRIGHTNESS("Luminosità", "brightness", R.drawable.ic_brightness),

    /**
     * The soil moisture parameter.
     */
    SOIL_MOISTURE("Umidità del suolo", "soilMoisture", R.drawable.ic_soil_moisture),

    /**
     * The humidity parameter.
     */
    HUMIDITY("Umidità dell'aria", "humidity", R.drawable.ic_humidity),

    /**
     * The temperature parameter.
     */
    TEMPERATURE("Temperatura", "temperature", R.drawable.ic_temperature);

    private final String title;
    private final String name;
    private final int imageId;

    ParameterType(String title, String name, int imageId) {
        this.title = new String(title.getBytes(), StandardCharsets.UTF_8);
        this.name = name;
        this.imageId = imageId;
    }

    /**
     * Gets the parameter's title.
     * @return the title of the parameter
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the parameter's name.
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the parameter's image path.
     * @return the image path
     */
    public int getImagePath() {
        return imageId;
    }

    /**
     * Returns an optional of parameter object from the given parameterName.
     * @param parameterName the parameter name
     * @return the optional of the parameter
     */
    public static Optional<ParameterType> parameterOf(String parameterName) {
        return Arrays.stream(ParameterType.values()).filter(p -> p.name.equals(parameterName)).findFirst();
    }
}
