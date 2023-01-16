package it.unibo.smartgh.entity.parameter;

import java.util.Date;

/**
 * Interface that represent a parameter value.
 */
public interface ParameterValue {

    /**
     * Get the greenhouse id.
     * @return the greenhouse id
     */
    String getGreenhouseId();

    /**
     * Get the date when the value is sensed.
     * @return the date when the value is sensed
     */
    Date getDate();

    /**
     * Get the plant value.
     * @return the plant value
     */
    Double getValue();

    /**
     * Get the parameter status.
     * @return the parameter status
     */
    String getStatus();

    /**
     * Set the greenhouse id.
     * @param greenhouseId the greenhouse id
     */
    void setGreenhouseId(String greenhouseId);

    /**
     * Set the plant value date.
     * @param date when the value is sensed
     */
    void setDate(Date date);

    /**
     * Set the value sensed.
     * @param value sensed
     */
    void setValue(Double value);

    /**
     * Set the parameter status.
     * @param status the parameter status
     */
    void setStatus(String status);

    String getUnit();

    void setUnit(String unit);
}
