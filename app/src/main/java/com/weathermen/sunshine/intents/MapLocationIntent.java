package com.weathermen.sunshine.intents;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class MapLocationIntent {
    private PackageManager packageManager;
    private String defaultLocationKey;
    private String locationKey;

    public MapLocationIntent(PackageManager packageManager, String locationKey, String defaultLocationKey) {
        this.packageManager = packageManager;
        this.defaultLocationKey = defaultLocationKey;
        this.locationKey = locationKey;
    }

    public Intent getIntent(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String location = sharedPreferences.getString(locationKey, defaultLocationKey);

        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(packageManager) != null) {
            return intent;
        } else {
            Log.d("Main", "couldn't call " + location + "No pref set");
        }
        return null;
    }
}