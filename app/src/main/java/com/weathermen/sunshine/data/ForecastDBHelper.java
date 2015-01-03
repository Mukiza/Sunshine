package com.weathermen.sunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ForecastDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "weather.db";
    private static final String TEXT_NOT_NULL = " TEXT NOT NULL ";
    private static final String DATE_NOT_NULL = " DATE NOT NULL ";
    public static final String COMMA = ",";
    public static final String INTEGER_PRIMARY_KEY_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static final String INTEGER_NOT_NULL = " INTEGER NOT NULL ";
    public static final String REAL_NOT_NULL = " REAL NOT NULL ";
    public static final String ON_CONFLICT_REPLACE = " ON CONFLICT REPLACE ";
    public static final String OPEN_BLACKET = "(";

    public ForecastDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_WEATHER_TABLE =
                "CREATE TABLE " + WeatherContract.WeatherEntry.TABLE_NAME

                        + OPEN_BLACKET
                        + WeatherContract.WeatherEntry._ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA
                        + WeatherContract.WeatherEntry.COLUMN_LOC_KEY + TEXT_NOT_NULL + COMMA
                        + WeatherContract.WeatherEntry.COLUMN_DATETEXT + DATE_NOT_NULL + COMMA
                        + WeatherContract.WeatherEntry.COLUMN_SHORT_DESC + TEXT_NOT_NULL + COMMA
                        + WeatherContract.WeatherEntry.COLUMN_WEATHER_ID + INTEGER_NOT_NULL + COMMA

                        + WeatherContract.WeatherEntry.COLUMN_MIN_TEMP + REAL_NOT_NULL + COMMA
                        + WeatherContract.WeatherEntry.COLUMN_MAX_TEMP + REAL_NOT_NULL + COMMA

                        + WeatherContract.WeatherEntry.COLUMN_HUMIDITY + REAL_NOT_NULL + COMMA
                        + WeatherContract.WeatherEntry.COLUMN_PRESSURE + REAL_NOT_NULL + COMMA
                        + WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + REAL_NOT_NULL + COMMA
                        + WeatherContract.WeatherEntry.COLUMN_DEGREES + REAL_NOT_NULL + COMMA

                        + "FOREIGN KEY (" + WeatherContract.WeatherEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                                WeatherContract.LocationEntry.TABLE_NAME + "(" + WeatherContract.LocationEntry._ID + "), "
                        + "UNIQUE (" + WeatherContract.WeatherEntry.COLUMN_DATETEXT + COMMA
                                + WeatherContract.WeatherEntry.COLUMN_LOC_KEY + ") " + ON_CONFLICT_REPLACE + ");";

        final String SQL_LOCATION_TABLE =
                "CREATE TABLE " + WeatherContract.LocationEntry.TABLE_NAME
                        + OPEN_BLACKET
                        + WeatherContract.LocationEntry._ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA

                        + WeatherContract.LocationEntry.COLUMN_LOCATION_NAME + TEXT_NOT_NULL + COMMA
                        + WeatherContract.LocationEntry.COLUMN_COORD_LAT + REAL_NOT_NULL + COMMA
                        + WeatherContract.LocationEntry.COLUMN_COORD_LONG + REAL_NOT_NULL + COMMA
                        + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + TEXT_NOT_NULL + COMMA
                        + "UNIQUE (" + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + ") " + ON_CONFLICT_REPLACE + ");";


        sqLiteDatabase.execSQL(SQL_LOCATION_TABLE);
        sqLiteDatabase.execSQL(SQL_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + WeatherContract.WeatherEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + WeatherContract.LocationEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
