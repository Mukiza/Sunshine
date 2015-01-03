package com.weathermen.sunshine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.weathermen.sunshine.data.ForecastDBHelper;
import com.weathermen.sunshine.data.WeatherContract;
import com.weathermen.sunshine.data.WeatherContract.WeatherEntry;


public class TestWeatherProvider extends AndroidTestCase {
    private static final String TEST_NAME = "North Pole";
    private static final String LOCATION_SETTING = "99705";
    private static final double TEST_LAT = 65.554;
    private static final double TEST_LNG = 65.554;
    private static final double TEST_PRESSURE = 1.3;
    private static final double TEST_HUMIDITY = 1.2;
    private static final double TEST_DEGREES = 1.1;
    private static final String TEST_DATE = "20141205";

    public static final String KAMPALA = "kampala";
    public static final String DATE = "20140612";
    private static SQLiteDatabase writeableSqLiteDatabase;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext.deleteDatabase(ForecastDBHelper.DATABASE_NAME);
        ForecastDBHelper forecastDBHelper = new ForecastDBHelper(mContext);
        writeableSqLiteDatabase = forecastDBHelper.getWritableDatabase();
    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(WeatherEntry.CONTENT_URI);
        assertEquals(WeatherEntry.CONTENT_TYPE, type);

        String weatherLocationType = mContext.getContentResolver().getType(WeatherEntry.buildWeatherLocation(KAMPALA));
        assertEquals(WeatherEntry.CONTENT_TYPE, weatherLocationType);

        String locationDateType = mContext.getContentResolver().getType(WeatherEntry.buildWeatherLocationWithDate(KAMPALA, DATE));
        assertEquals(WeatherEntry.CONTENT_ITEM_TYPE, locationDateType);


        String locationType = mContext.getContentResolver().getType(WeatherContract.LocationEntry.CONTENT_URI);
        assertEquals(WeatherContract.LocationEntry.CONTENT_TYPE, locationType);

        String singleLocationType = mContext.getContentResolver().getType(WeatherContract.LocationEntry.buildLocationUri(1L));
        assertEquals(WeatherContract.LocationEntry.CONTENT_ITEM_TYPE, singleLocationType);
    }

    public void testQueryLocationData() {
        ContentValues contentValues = TestDb.getLocationContentValues(TEST_NAME, LOCATION_SETTING, TEST_LAT, TEST_LNG);

        TestDb.persistContentValues(WeatherContract.LocationEntry.TABLE_NAME, contentValues, writeableSqLiteDatabase);

        Cursor cursor = mContext.getContentResolver().query(
                WeatherContract.LocationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            TestDb.validateCursor(contentValues, cursor);
        }
    }


    public void testQueryWeatherData() {
        ContentValues weatherContentValues = createLocation();

        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (weatherCursor.moveToFirst()) {
            TestDb.validateCursor(weatherContentValues, weatherCursor);
        }
    }


    public void testQueryJoinedLocationAndWeatherData() throws Exception {
        ContentValues weatherContentValues = createLocation();

        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithStartDate(LOCATION_SETTING, TEST_DATE),
                null,
                null,
                null,
                null
        );
        if (weatherCursor.moveToFirst()) {
            TestDb.validateCursor(weatherContentValues, weatherCursor);
        }
    }

    private ContentValues createLocation() {
        ContentValues contentValues = TestDb.getLocationContentValues(TEST_NAME, LOCATION_SETTING, TEST_LAT, TEST_LNG);
        long locationID = TestDb.persistContentValues(WeatherContract.LocationEntry.TABLE_NAME, contentValues, writeableSqLiteDatabase);

        ContentValues weatherContentValues = TestDb.getWeatherContentValues(locationID);
        TestDb.persistContentValues(WeatherEntry.TABLE_NAME, weatherContentValues, writeableSqLiteDatabase);
        return weatherContentValues;
    }
}
