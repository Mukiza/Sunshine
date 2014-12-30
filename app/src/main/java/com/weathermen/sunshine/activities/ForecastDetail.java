package com.weathermen.sunshine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.weathermen.sunshine.R;
import com.weathermen.sunshine.factories.intents.MapLocationIntent;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_map) {

            MapLocationIntent mapIntentFactory = new MapLocationIntent(
                    getPackageManager(),
                    getString(R.string.pref_location_key),
                    getString(R.string.pref_location_default)
            );

            Intent intent = mapIntentFactory.getIntent(this);
            if (intent != null) {
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
