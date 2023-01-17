package it.unibo.smartgh.presentation.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import it.unibo.smartgh.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.entity.plant.PlantImpl;

/**
 * Custom {@link JsonDeserializer} for the {@link Greenhouse} class. Used to convert a JSON object
 * into an {@link Greenhouse} object.
 */
public class GreenhouseDeserializer  extends GeneralDeserializer implements JsonDeserializer<Greenhouse> {

    @Override
    public Greenhouse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Greenhouse greenhouse = null;
        if(json instanceof JsonObject){
            JsonObject object = (JsonObject) json;
            Plant plant = this.getPropertyAs(object, "plant", PlantImpl.class, context);
            Modality modality = Modality.valueOf(this.getPropertyAsString(object, "modality"));
            greenhouse = new GreenhouseImpl(plant, modality);
        }else{
            throw new JsonParseException("Not a valid greenhouse");
        }
        return greenhouse;
    }
}
