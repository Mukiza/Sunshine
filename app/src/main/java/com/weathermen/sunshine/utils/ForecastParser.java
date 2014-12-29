package com.weathermen.sunshine.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ForecastParser {
    private String unitType;
    private String forecastJsonStr;

    public ForecastParser(String unitType, String forecastJsonStr){
        this.unitType = unitType;
        this.forecastJsonStr = forecastJsonStr;
    }

    private static String getReadableDateString(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        return format.format(date);
    }

    private String formatHighLows(double high, double low) {
        String IMPERIAL = "imperial";

        if (this.unitType.equals(IMPERIAL)) {
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        }
        return Math.round(high) + "/" + Math.round(low);
    }

    public  String[] getWeatherDataFromJson()
            throws JSONException {

        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DATETIME = "dt";
        final String OWM_DESCRIPTION = "main";

        JSONObject forecastJson = new JSONObject(this.forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
        int jsonResultLen = weatherArray.length();
        String[] resultString = new String[jsonResultLen];

        for (int i = 0; i < weatherArray.length(); i++) {
            String day;
            String description;
            String highAndLow;

            JSONObject dayForecast = weatherArray.getJSONObject(i);

            long dateTime = dayForecast.getLong(OWM_DATETIME);
            day = getReadableDateString(dateTime);

            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);

            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = this.formatHighLows(high, low);
            resultString[i] = day + " - " + description + " - " + highAndLow;
        }

        return resultString;
    }

}
