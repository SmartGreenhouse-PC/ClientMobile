package it.unibo.smartgh.view.selectGreenhouse.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import it.unibo.smartgh.R;
import it.unibo.smartgh.view.recyclerview.Adapter;
import it.unibo.smartgh.view.recyclerview.OnItemListener;

/**
 * A class that represents the greenhouse adapter.
 */
public class SelectGreenhouseAdapter extends RecyclerView.Adapter<SelectGreenhouseViewHolder> implements Adapter<String> {
    private List<String> greenhouseList;
    private final OnItemListener listener;

    /**
     * Constructor of {@link SelectGreenhouseAdapter}.
     * @param listener the listener associated.
     */
    public SelectGreenhouseAdapter(OnItemListener listener){
        this.greenhouseList = new LinkedList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectGreenhouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.greenhouse_card_layout, parent, false);
        return new SelectGreenhouseViewHolder(layoutView, this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectGreenhouseViewHolder holder, int position) {
        final String greenhouseName = this.greenhouseList.get(position);
        holder.getGreenhouseName().setText(greenhouseName);
    }

    @Override
    public int getItemCount() {
        return this.greenhouseList.size();
    }

    @Override
    public void setData(List<String> data) {
        System.out.println("ADAPTER " + data);
        this.greenhouseList = data;
        notifyDataSetChanged();
    }

    public List<String> getData(){
        return this.greenhouseList;
    }
}
