package com.weathermen.sunshine.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.weathermen.sunshine.R;
import com.weathermen.sunshine.activities.ForecastDetail;
import com.weathermen.sunshine.activities.Settings;
import com.weathermen.sunshine.services.ForecastService;
import com.weathermen.sunshine.utils.ForecastParser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForecastFragment extends Fragment {
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onStart() {
        updateForecasts();
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_forecast_fragment, menu);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.action_refresh) {
                updateForecasts();
                return true;
            }
        return super.onOptionsItemSelected(item);
    }

    private void updateForecasts() {
        FetchForecastTask fetchForecastTask = new FetchForecastTask();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String location = sharedPreferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));

        fetchForecastTask.execute(location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        adapter = getForecastArrayAdapter(getActivity(), new ArrayList<String>());
        new FetchForecastTask().execute("Kampala,uganda");

        ListView view = (ListView) rootView.findViewById(R.id.list_item_forecasts);
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), ForecastDetail.class);
                String selectedForecast = (String) adapterView.getItemAtPosition(position);

                intent.putExtra(Intent.EXTRA_TEXT, selectedForecast);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private static ArrayAdapter<String> getForecastArrayAdapter(FragmentActivity activity, List<String> items) {
        return new ArrayAdapter<String>(
                activity,
                R.layout.forecast_item_list,
                R.id.list_item_forecast_textView,
                items
        );
    }

    public class FetchForecastTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }
            String forecastJsonStr = new ForecastService().getForecasts(params);
            try {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String unitType = sharedPreferences
                        .getString(getString(R.string.pref_units_key),
                        getString(R.string.pref_units_metric));

                return new ForecastParser(unitType, forecastJsonStr).getWeatherDataFromJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            List<String> forecasts = new ArrayList<String>(Arrays.asList(strings));
            adapter.clear();
            adapter.addAll(forecasts);
        }
    }
}


