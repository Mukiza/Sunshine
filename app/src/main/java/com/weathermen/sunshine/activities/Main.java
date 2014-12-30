package com.weathermen.sunshine.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

import com.weathermen.sunshine.R;
import com.weathermen.sunshine.factories.ImplicitMapIntentFactory;
import com.weathermen.sunshine.fragments.ForecastFragment;


public class Main extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                return true;
            case R.id.action_map:

                ImplicitMapIntentFactory mapIntentFactory = new ImplicitMapIntentFactory(
                        getPackageManager(),
                        getString(R.string.pref_location_key),
                        getString(R.string.pref_location_default)
                );

                Intent intent = mapIntentFactory.getIntent(this);
                if (intent != null) {
                    startActivity(intent);
                    return true;
                }
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}