package it.unibo.smartgh.view.recyclerview;

/**
 * Listener for elements in a recycler view.
 */
public interface OnItemListener {

    void onItemClick(int position);

    boolean onItemLongClick(int position);

}
