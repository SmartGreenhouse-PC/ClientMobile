package it.unibo.smartgh.entity.greenhouse;


import it.unibo.smartgh.entity.plant.Plant;

/**
 * Implementation of the greenhouse entity.
 */
public class GreenhouseImpl implements Greenhouse {

    private final Plant plant;
    private final Modality modality;
    private String id;

    /**
     * Constructor for the greenhouse entity.
     * @param plant of the greenhouse
     * @param modality the actual modality of management
     */
    public GreenhouseImpl(Plant plant, Modality modality) {
        this.plant = plant;
        this.modality = modality;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Plant getPlant() {
        return this.plant;
    }

    @Override
    public Modality getActualModality() {
        return this.modality;
    }

    public void setId(String id){
        this.id = id;
    }
}
