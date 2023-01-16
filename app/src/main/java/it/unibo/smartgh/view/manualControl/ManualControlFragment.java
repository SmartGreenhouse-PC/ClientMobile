package it.unibo.smartgh.view.manualControl;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.view.manualControl.adapter.OperationAdapter;
import it.unibo.smartgh.viewmodel.GreenhouseViewModel;
import it.unibo.smartgh.viewmodel.GreenhouseViewModelImpl;
import it.unibo.smartgh.viewmodel.OperationViewModel;
import it.unibo.smartgh.viewmodel.OperationViewModelImpl;

public class ManualControlFragment extends Fragment {
    private OperationAdapter operationAdapter;

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
            //todo setup toolbar
            setRecyclerView();
            final OperationViewModel operationViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(OperationViewModelImpl.class);
            this.operationAdapter.setOperationViewModel(operationViewModel);
            SwitchCompat manualControlSwitch = view.findViewById(R.id.manualControlChoice);
            manualControlSwitch.setOnCheckedChangeListener((button, isChecked) ->{
                if(isChecked){
                    operationViewModel.getLastParameterOperation(ParameterType.BRIGHTNESS.getName());
                    operationViewModel.getLastParameterOperation(ParameterType.TEMPERATURE.getName());
                    operationViewModel.getLastParameterOperation(ParameterType.HUMIDITY.getName());
                    operationViewModel.getLastParameterOperation(ParameterType.SOIL_MOISTURE.getName());
                }
                this.operationAdapter.updateModality(isChecked);
                //todo passaggio a modalità diversa
            });
            final GreenhouseViewModel greenhouseViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(GreenhouseViewModelImpl.class);
            //operationViewModel.sendNewOperation("temperature", "TEMPERATURE increase");
            operationViewModel.getAllLastOperationsParameter().observe((LifecycleOwner) activity, map -> {
               operationAdapter.setLastOperation(map);
            });
            greenhouseViewModel.getPlantLiveData().observe((LifecycleOwner) activity, plant -> operationAdapter.setPlant(plant));
            greenhouseViewModel.getParameterValueLiveData().observe((LifecycleOwner) activity, map ->{
                System.out.println("New data arrived.");
                List<Pair<ParameterType, ParameterValue>> list = new ArrayList<>();
                map.forEach((k,v) -> list.add(new Pair<>(k,v)));
               operationAdapter.setData(list);
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //todo
    }

    private void setRecyclerView(){
        final RecyclerView recyclerView = requireView().findViewById(R.id.manual_control_recycler_view);
        recyclerView.setHasFixedSize(true);
        this.operationAdapter = new OperationAdapter(getActivity());
        recyclerView.setAdapter(this.operationAdapter);
    }
}
