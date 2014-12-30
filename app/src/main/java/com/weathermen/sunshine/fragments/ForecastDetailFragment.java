package com.weathermen.sunshine.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weathermen.sunshine.R;
import com.weathermen.sunshine.activities.Settings;
import com.weathermen.sunshine.intents.ShareIntent;

public class ForecastDetailFragment extends Fragment {
    private final String LOG_TAG = ForecastDetailFragment.class.getSimpleName();
    private String forecastDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        inflater.inflate(R.menu.menu_share, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if(shareActionProvider != null) {
            shareActionProvider.setShareIntent(new ShareIntent(forecastDetail).getIntent());
        }else {
            Log.d(LOG_TAG, "Share action provider is null");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), Settings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast_detail, container, false);

        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            forecastDetail = intent.getStringExtra(Intent.EXTRA_TEXT);
            TextView view = (TextView) rootView.findViewById(R.id.forecast_detail_text);
            view.setText(forecastDetail);
        }
        return rootView;
    }
}
