package it.unibo.smartgh.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import it.unibo.smartgh.R;
import it.unibo.smartgh.utility.ActivityUtilities;
import it.unibo.smartgh.view.homepage.HomeFragment;
import it.unibo.smartgh.view.manualControl.ManualControlFragment;
import it.unibo.smartgh.view.selectGreenhouse.SelectGreenhouseFragment;

/**
 * This class represents the main activity of the application.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            ActivityUtilities.insertFragment(this, new SelectGreenhouseFragment(), SelectGreenhouseFragment.class.getSimpleName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.isVisible()) {
            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
                return  true;
            }
        }
        return false;
    }

}