package it.unibo.smartgh.view.manualControl.adapter.manager.impl;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.core.content.ContextCompat;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.view.manualControl.adapter.OperationAdapter;
import it.unibo.smartgh.view.manualControl.adapter.OperationViewHolder;

public class BrightnessHolderManager extends AbstractParameterHolderManager {
    public static final String LUMINOSITY = "LUMINOSITY ";

    private OperationViewHolder holder;
    private final SeekBar seekbar;

    public BrightnessHolderManager(Activity activity, OperationAdapter adapter){
        super(activity, adapter);
        this.seekbar = new SeekBar(this.activity.getApplicationContext());
    }

    @Override
    public void setHolder(OperationViewHolder holder) {
        this.holder = holder;
    }

    @Override
    public void setElement() {
        Drawable drawable = ContextCompat.getDrawable(this.activity, ParameterType.BRIGHTNESS.getImagePath());
        holder.getParameterImage().setImageDrawable(drawable);
        holder.getOptimalRange().setText(plant.getMinBrightness() + " - " + plant.getMaxBrightness() + plant.getUnitMap().get("brightness"));
        this.setBrightnessOperationElement();
    }

    @Override
    public void setManualModality(Boolean modality) {
        super.setManualModality(modality);
        this.seekbar.setEnabled(this.modality);
    }

    public void setLampBrightnessLevel(int progress) {
        this.seekbar.setProgress(progress);
    }

    private void setBrightnessOperationElement() {
        int max = 255;
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
        };

        int[] colors = new int[] {
                ContextCompat.getColor(activity, R.color.brightness),
                Color.LTGRAY,
        };
        this.seekbar.setEnabled(this.modality);
        this.seekbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.seekbar.setProgressTintList(new ColorStateList(states, colors));
        this.seekbar.setMax(max);
        this.seekbar.setProgress(max/2);
        this.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int seekbarValue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) { this.seekbarValue = i; }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setLampBrightnessLevel(seekbarValue);
                adapter.sendOperation(ParameterType.BRIGHTNESS.getName(), LUMINOSITY + seekbarValue);
            }
        });
        System.out.println("Brightness adapter:" + this.holder.getOperationsLayout().getChildCount());
        this.holder.getOperationsLayout().addView(this.seekbar);
        this.set = true;
    }
}
