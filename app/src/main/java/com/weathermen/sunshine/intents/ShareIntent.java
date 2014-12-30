package com.weathermen.sunshine.intents;

import android.content.Intent;

public class ShareIntent {

    private String forecast;

    public ShareIntent(String forecast){
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
