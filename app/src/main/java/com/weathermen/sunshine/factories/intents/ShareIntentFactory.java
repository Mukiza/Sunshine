package com.weathermen.sunshine.factories.intents;

import android.content.Intent;

public class ShareIntentFactory {

    private String forecast;

    public ShareIntentFactory(String forecast){
        this.forecast = forecast;
    }

    public Intent getIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String forecastHashTag = "#sunShineApp";
        shareIntent.putExtra(Intent.EXTRA_TEXT, this.forecast + forecastHashTag);
        return shareIntent;
    }
}
