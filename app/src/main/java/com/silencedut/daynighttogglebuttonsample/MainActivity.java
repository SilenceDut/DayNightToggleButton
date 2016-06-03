package com.silencedut.daynighttogglebuttonsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.silencedut.daynighttogglebutton.DayNightToggleButton;
import com.silencedut.daynighttogglebutton.ToggleSettings;
import com.silencedut.daynighttogglebuttonsample.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DayNightToggleButton mButtonWithBuilder = (DayNightToggleButton)findViewById(R.id.day_night_with_builder);
        ToggleSettings mBuilderSettings = new ToggleSettings.Builder()
                .setToggleUnCheckedColor(getResources().getColor(R.color.colorAccent))
                .setBackgroundUncheckedColor(getResources().getColor(R.color.blue500))
                .buildSettings();
        mButtonWithBuilder.setToggleSettings(mBuilderSettings);


    }
}
