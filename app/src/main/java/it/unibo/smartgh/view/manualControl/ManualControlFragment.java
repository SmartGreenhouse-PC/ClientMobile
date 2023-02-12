package it.unibo.smartgh.view.manualControl;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.utility.ActivityUtilities;
import it.unibo.smartgh.view.manualControl.adapter.OperationAdapter;
import it.unibo.smartgh.viewmodel.GreenhouseViewModel;
import it.unibo.smartgh.viewmodel.GreenhouseViewModelImpl;
import it.unibo.smartgh.viewmodel.OperationViewModel;
import it.unibo.smartgh.viewmodel.OperationViewModelImpl;

/**
 * A class that represents the manual control fragment.
 */
public class ManualControlFragment extends Fragment {
    private OperationAdapter operationAdapter;
    private GreenhouseViewModel greenhouseViewModel;
    private final String greenhouseId;

    /**
     * Public constructor for the ManualControlFragment.
     * @param greenhouseId the current greenhouse id.
     */
    public ManualControlFragment(String greenhouseId) {
        super();
        this.greenhouseId = greenhouseId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manual_control_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final Activity activity  = getActivity();
        if(activity != null){
            ActivityUtilities.setUpToolbar((AppCompatActivity) activity, "Controllo manuale");
            ActivityUtilities.setVisibleToolbarNavigationIcon((AppCompatActivity) activity, true);
            setRecyclerView();
            final OperationViewModel operationViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(OperationViewModelImpl.class);
            operationViewModel.setGreenhouseId(this.greenhouseId);
            greenhouseViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(GreenhouseViewModelImpl.class);
            greenhouseViewModel.initializeModalitySocket();
            this.operationAdapter.setOperationViewModel(operationViewModel);
            SwitchCompat manualControlSwitch = view.findViewById(R.id.manualControlChoice);
            greenhouseViewModel.getModalityLiveData().observe((LifecycleOwner) activity, modality -> {
                boolean isManual = modality.equals(Modality.MANUAL);
                if(isManual){
                    operationViewModel.getLastParameterOperation(ParameterType.BRIGHTNESS.getName());
                    operationViewModel.getLastParameterOperation(ParameterType.TEMPERATURE.getName());
                    operationViewModel.getLastParameterOperation(ParameterType.HUMIDITY.getName());
                    operationViewModel.getLastParameterOperation(ParameterType.SOIL_MOISTURE.getName());
                }
                manualControlSwitch.setChecked(isManual);
                this.operationAdapter.updateModality(isManual);
            });
            manualControlSwitch.setOnCheckedChangeListener((button, isChecked) ->{
                if(isChecked){
                    operationViewModel.getLastParameterOperation(ParameterType.BRIGHTNESS.getName());
                    operationViewModel.getLastParameterOperation(ParameterType.TEMPERATURE.getName());
                    operationViewModel.getLastParameterOperation(ParameterType.HUMIDITY.getName());
                    operationViewModel.getLastParameterOperation(ParameterType.SOIL_MOISTURE.getName());
                    greenhouseViewModel.changeModality(Modality.MANUAL);
                } else {
                    greenhouseViewModel.changeModality(Modality.AUTOMATIC);
                }
            });
            operationViewModel.getAllLastOperationsParameter().observe((LifecycleOwner) activity, map -> operationAdapter.setLastOperation(map));
            greenhouseViewModel.getPlantLiveData().observe((LifecycleOwner) activity, plant -> operationAdapter.setPlant(plant));
            greenhouseViewModel.getParametersLiveData().observe((LifecycleOwner) activity, list -> operationAdapter.setData(list));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        this.greenhouseViewModel.closeModalitySocket();
        super.onDestroyView();
    }

    private void setRecyclerView(){
        final RecyclerView recyclerView = requireView().findViewById(R.id.manual_control_recycler_view);
        recyclerView.setHasFixedSize(true);
        this.operationAdapter = new OperationAdapter(getActivity());
        recyclerView.setAdapter(this.operationAdapter);
    }
}
