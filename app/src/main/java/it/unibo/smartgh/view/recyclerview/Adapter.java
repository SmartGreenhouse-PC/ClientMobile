package it.unibo.smartgh.view.recyclerview;

import java.util.List;

/**
 * Interface to manage the recycler view adapter.
 * @param <T> the adapter data class
 */
public interface Adapter<T> {

    /**
     * Set the list of the data to show.
     * @param data the list of data
     */
    void setData(List<T> data);
}