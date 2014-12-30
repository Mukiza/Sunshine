package com.weathermen.sunshine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.weathermen.sunshine.data.ForecastContract.WeatherEntry;
import com.weathermen.sunshine.data.ForecastContract.LocationEntry;
import com.weathermen.sunshine.data.ForecastDBHelper;


public class TestDb extends AndroidTestCase {
    static final String testName = "North Pole";
    static final String locationSetting = "99705";
    static final double testLat = 65.554;
    static final double testLng = 65.554;
    public static final double TEST_PRESSURE = 1.3;
    public static final double TEST_HUMIDITY = 1.2;
    public static final double TEST_DEGREES = 1.1;
    public static final String TEST_DATE = "20141205";


    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(ForecastDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new ForecastDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadLocationData() {
        ForecastDBHelper forecastDBHelper = new ForecastDBHelper(mContext);
        SQLiteDatabase db = forecastDBHelper.getWritableDatabase();

        long locationRowId = persistLocation(testName, locationSetting, testLat, testLng, db);

        assertTrue(locationRowId != -1);


        String[] columns = {
                LocationEntry.COLUMN_LOCATION_SETTING,
                LocationEntry.COLUMN_LOCATION_NAME,
                LocationEntry.COLUMN_COORD_LONG,
                LocationEntry.COLUMN_COORD_LAT
        };

        Cursor cursor = db.query(LocationEntry.TABLE_NAME,
                columns,
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int locationIndex = cursor.getColumnIndex(LocationEntry.COLUMN_LOCATION_SETTING);
            String location = cursor.getString(locationIndex);

            int nameIndex = cursor.getColumnIndex(LocationEntry.COLUMN_LOCATION_NAME);
            String name = cursor.getString(nameIndex);

            int latIndex = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LAT);
            double latitude = cursor.getDouble(latIndex);

            int lngIndex = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LONG);
            double longitude = cursor.getDouble(lngIndex);


            assertEquals(name, testName);
            assertEquals(testLat, latitude);
            assertEquals(testLng, longitude);
            assertEquals(location, locationSetting);
        }
    }

    public void testInsertWeatherInDb() {
        ForecastDBHelper forecastDBHelper = new ForecastDBHelper(mContext);
        SQLiteDatabase db = forecastDBHelper.getWritableDatabase();

        long locationRowId = persistLocation(testName, locationSetting, testLat, testLng, db);

        ContentValues contentValues = getWeatherContentValues(locationRowId);
        long weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, contentValues);
        assertTrue(weatherRowId != -1);

        String[] columns = {
                WeatherEntry.COLUMN_LOC_KEY,
                WeatherEntry.COLUMN_DATETEXT,
                WeatherEntry.COLUMN_SHORT_DESC,
                WeatherEntry.COLUMN_WEATHER_ID,
                WeatherEntry.COLUMN_MIN_TEMP,
                WeatherEntry.COLUMN_MAX_TEMP,
                WeatherEntry.COLUMN_HUMIDITY,
                WeatherEntry.COLUMN_PRESSURE,
                WeatherEntry.COLUMN_WIND_SPEED,
                WeatherEntry.COLUMN_DEGREES
        };

        Cursor cursor = db.query(WeatherEntry.TABLE_NAME,
                columns,
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int dateIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_DATETEXT);
            String date = cursor.getString(dateIndex);

            int degreesIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_DEGREES);
            double degrees = cursor.getDouble(degreesIndex);

            int humidityIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY);
            double humidity = cursor.getDouble(humidityIndex);

            int pressureIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_PRESSURE);
            double pressure = cursor.getDouble(pressureIndex);


            assertEquals(pressure, TEST_PRESSURE);
            assertEquals(humidity, TEST_HUMIDITY);
            assertEquals(degrees, TEST_DEGREES);
            assertEquals(date, TEST_DATE);
        }
    }

    private ContentValues getLocationContentValues(String testName, String locationSetting, double testLat, double testLng) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(LocationEntry.COLUMN_LOCATION_NAME, testName);
        contentValues.put(LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
        contentValues.put(LocationEntry.COLUMN_COORD_LAT, testLat);
        contentValues.put(LocationEntry.COLUMN_COORD_LONG, testLng);
        return contentValues;
    }

    private ContentValues getWeatherContentValues(long locationRowId) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        contentValues.put(WeatherEntry.COLUMN_DATETEXT, TEST_DATE);
        contentValues.put(WeatherEntry.COLUMN_DEGREES, TEST_DEGREES);
        contentValues.put(WeatherEntry.COLUMN_HUMIDITY, TEST_HUMIDITY);
        contentValues.put(WeatherEntry.COLUMN_PRESSURE, TEST_PRESSURE);
        contentValues.put(WeatherEntry.COLUMN_MAX_TEMP, 74);
        contentValues.put(WeatherEntry.COLUMN_MIN_TEMP, 87);
        contentValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Stuff");
        contentValues.put(WeatherEntry.COLUMN_WIND_SPEED, 4.4);
        contentValues.put(WeatherEntry.COLUMN_WEATHER_ID, 322);
        return contentValues;
    }

    private long persistLocation(String testName, String locationSetting, double testLat, double testLng, SQLiteDatabase db) {
        ContentValues contentValues = getLocationContentValues(testName, locationSetting, testLat, testLng);
        return db.insert(LocationEntry.TABLE_NAME, null, contentValues);
    }

}