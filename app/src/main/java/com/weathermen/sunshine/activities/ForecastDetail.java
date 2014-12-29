package com.weathermen.sunshine.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.weathermen.sunshine.R;
import com.weathermen.sunshine.fragments.ForecastDetailFragment;

public class ForecastDetail extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastDetailFragment())
                    .commit();
        }

    }
}
