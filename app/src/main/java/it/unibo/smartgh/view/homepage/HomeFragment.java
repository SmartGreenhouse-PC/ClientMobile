package it.unibo.smartgh.view.homepage;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import it.unibo.smartgh.R;
import it.unibo.smartgh.utility.ActivityUtilities;
import it.unibo.smartgh.view.homepage.adapter.HomepageParameterAdapter;
import it.unibo.smartgh.viewmodel.GreenhouseViewModel;
import it.unibo.smartgh.viewmodel.GreenhouseViewModelImpl;

public class HomeFragment extends Fragment {

    private Activity activity;
    private HomepageParameterAdapter homepageParameterAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homepage_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();
        if (activity != null) {
            ActivityUtilities.setUpToolbar((AppCompatActivity) activity, "Smart Greenhouse");
            setRecyclerView();
            final TextView plantName = view.findViewById(R.id.plant_name);
            final ImageView plantImage = view.findViewById(R.id.plant_image);
            final TextView greenhouseStatus = view.findViewById(R.id.greenhouse_status);
            final GreenhouseViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(GreenhouseViewModelImpl.class);
            viewModel.getPlantLiveData().observe((LifecycleOwner) activity, plant -> {
                Picasso.get().load(plant.getImg()).into(plantImage);
                plantName.setText(plant.getName());
            });
            viewModel.getParametersLiveData().observe((LifecycleOwner) activity, homepageParameterAdapter::setData);
            viewModel.getStatusLiveData().observe((LifecycleOwner) activity, state -> {
                greenhouseStatus.setText(state);
                if (state.equals("ALLARME")) {
                    greenhouseStatus.setTextColor(ContextCompat.getColor(activity, R.color.alarm));
                    greenhouseStatus.setBackgroundColor(ContextCompat.getColor(activity, R.color.alarm_background));
                } else {
                    greenhouseStatus.setTextColor(ContextCompat.getColor(activity, R.color.normal));
                    greenhouseStatus.setBackgroundColor(Color.TRANSPARENT);
                }
            });
        }
    }

    private void setRecyclerView() {
        final RecyclerView recyclerView = requireView().findViewById(R.id.housework_recycler_view);
        recyclerView.setHasFixedSize(true);
        this.homepageParameterAdapter = new HomepageParameterAdapter(this.activity);
        recyclerView.setAdapter(this.homepageParameterAdapter);
    }

    /**
     * Set the next view to show.
     */
    public void setNextView() {

    }
}
