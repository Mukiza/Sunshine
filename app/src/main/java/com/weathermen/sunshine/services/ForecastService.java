package com.weathermen.sunshine.services;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForecastService {
    private final String LOG_TAG = ForecastService.class.getSimpleName();

    public String getForecasts(String[] params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            URL url = new URL(getUri(params).toString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            return builder.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    private Uri getUri(String [] params) {
        final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";

        return Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("q", params[0])
                .appendQueryParameter("mode", "json")
                .appendQueryParameter("units", "metric")
                .appendQueryParameter("cnt", Integer.toString(7))
                .build();
    }
}
