package it.unibo.smartgh.view.homepage;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.utility.ActivityUtilities;
import it.unibo.smartgh.viewmodel.GreenhouseViewModel;
import it.unibo.smartgh.viewmodel.GreenhouseViewModelImpl;

public class HomeFragment extends Fragment {

    private Activity activity;

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
            final TextView plantName = view.findViewById(R.id.plant_name);
            GreenhouseViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(GreenhouseViewModelImpl.class);
            viewModel.getParameterValueLiveData().observe((LifecycleOwner) activity, map -> {
                plantName.setText(map.get(ParameterType.TEMPERATURE).getValue().toString());
            });
        }
    }

    /**
     * Set the next view to show.
     */
    public void setNextView() {

    }
}
