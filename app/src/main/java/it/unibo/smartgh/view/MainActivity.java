package it.unibo.smartgh.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import it.unibo.smartgh.R;
import it.unibo.smartgh.utility.ActivityUtilities;
import it.unibo.smartgh.view.homepage.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            //insert first fragment.
            ActivityUtilities.insertFragment(this, new HomeFragment(), HomeFragment.class.getSimpleName());
        }
    }
}