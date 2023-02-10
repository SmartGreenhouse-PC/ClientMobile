package it.unibo.smartgh.view.selectGreenhouse;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import it.unibo.smartgh.R;
import it.unibo.smartgh.utility.ActivityUtilities;
import it.unibo.smartgh.view.homepage.HomeFragment;
import it.unibo.smartgh.view.recyclerview.OnItemListener;
import it.unibo.smartgh.view.selectGreenhouse.adapter.SelectGreenhouseAdapter;
import it.unibo.smartgh.viewmodel.GreenhouseViewModel;
import it.unibo.smartgh.viewmodel.GreenhouseViewModelImpl;

public class SelectGreenhouseFragment extends Fragment implements OnItemListener {

    private Activity activity;
    private SelectGreenhouseAdapter selectGreenhouseAdapter;
    private GreenhouseViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.select_greenhouse_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = getActivity();
        if(activity != null){
            ActivityUtilities.setUpToolbar((AppCompatActivity) activity, "Smart Greenhouse");
            ActivityUtilities.setVisibleToolbarNavigationIcon((AppCompatActivity) activity, false);
            setRecyclerView();
            this.viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(GreenhouseViewModelImpl.class);
            this.viewModel.getAllGreenhouses().observe((LifecycleOwner) activity, this.selectGreenhouseAdapter::setData);
        }
    }

    private void setRecyclerView(){
        final RecyclerView recyclerView = requireView().findViewById(R.id.select_greenhouse_recycler_view);
        recyclerView.setHasFixedSize(true);
        this.selectGreenhouseAdapter = new SelectGreenhouseAdapter(this);
        recyclerView.setAdapter(this.selectGreenhouseAdapter);
    }

    @Override
    public void onItemClick(int position) {
        this.viewModel.setGreenhouseId(this.selectGreenhouseAdapter.getData().get(position));
        ActivityUtilities.insertFragment((AppCompatActivity) activity, new HomeFragment(), HomeFragment.class.getSimpleName());
    }

    @Override
    public boolean onItemLongClick(int position) {
        return false;
    }
}
