package it.unibo.smartgh.utility;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


import it.unibo.smartgh.R;
import it.unibo.smartgh.view.homepage.HomeFragment;
import it.unibo.smartgh.view.selectGreenhouse.SelectGreenhouseFragment;


/**
 * This class represents the utilities for activity.
 */
public class ActivityUtilities {

    /**
     * Gets the server configuration.
     * @param context current context
     * @return the server configuration
     */
    public static Config getConfig(Context context) {
        final Gson gson = new Gson();
        final InputStream rawResource = context.getResources().openRawResource(R.raw.config);
        final String result = new BufferedReader(new InputStreamReader(rawResource)).lines().parallel().collect(Collectors.joining("\n"));
        return gson.fromJson(result, ConfigImpl.class);
    }

    /**
     * Insert a fragment in the view.
     * @param activity the current activity
     * @param fragment the fragment to insert
     * @param tag the tag of the fragment
     */
    public static void insertFragment(AppCompatActivity activity, Fragment fragment, String tag) {
        final FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment, tag);
        if (!(fragment instanceof SelectGreenhouseFragment)) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    /**
     * Set up the toolbar with application title.
     * @param activity the current activity
     */
    public static void setUpToolbar(AppCompatActivity activity) {
        setUpToolbar(activity, activity.getResources().getString(R.string.app_name));
    }

    /**
     * Set up the toolbar with a custom title
     * @param activity the current activity
     * @param title the title to set
     */
    public static void setUpToolbar(AppCompatActivity activity, String title) {
        final Toolbar toolbar = activity.findViewById(R.id.top_app_bar);
        toolbar.setTitle(title);
        if (activity.getSupportActionBar() == null) {
            activity.setSupportActionBar(toolbar);
        }
    }

    /**
     * Set the navigation icon in the toolbar
     * @param activity the current activity
     * @param visible set the visibility of the icon
     */
    public static void setVisibleToolbarNavigationIcon(AppCompatActivity activity, boolean visible) {
        final Toolbar toolbar = activity.findViewById(R.id.top_app_bar);
        activity.setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (activity.getSupportActionBar() != null){
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(visible);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(visible);
        }
    }

}
