package com.weathermen.sunshine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.weathermen.sunshine.data.WeatherContract.WeatherEntry;
import com.weathermen.sunshine.data.WeatherContract.LocationEntry;
import com.weathermen.sunshine.data.ForecastDBHelper;

import java.util.Map;
import java.util.Set;


public class TestDb extends AndroidTestCase {
    private static final String TEST_NAME = "North Pole";
    private static final String LOCATION_SETTING = "99705";
    private static final double TEST_LAT = 65.554;
    private static final double TEST_LNG = 65.554;
    private static final double TEST_PRESSURE = 1.3;
    private static final double TEST_HUMIDITY = 1.2;
    private static final double TEST_DEGREES = 1.1;
    private static final String TEST_DATE = "20141205";


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
        ContentValues contentValues = getLocationContentValues(TEST_NAME, LOCATION_SETTING, TEST_LAT, TEST_LNG);
        long locationRowId = persistContentValues(LocationEntry.TABLE_NAME, contentValues, db);

        assertTrue(locationRowId != -1);

        Cursor cursor = db.query(LocationEntry.TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            validateCursor(contentValues, cursor);        }
    }

    public void testInsertReadWeatherDataInDb() {
        ForecastDBHelper forecastDBHelper = new ForecastDBHelper(mContext);
        SQLiteDatabase db = forecastDBHelper.getWritableDatabase();
        ContentValues locationContentValues = getLocationContentValues(TEST_NAME, LOCATION_SETTING, TEST_LAT, TEST_LNG);
        long locationRowId = persistContentValues(LocationEntry.TABLE_NAME, locationContentValues, db);

        ContentValues contentValues = getWeatherContentValues(locationRowId);
        long weatherRowId = persistContentValues(WeatherEntry.TABLE_NAME, contentValues, db);

        assertTrue(weatherRowId != -1);

        Cursor cursor = db.query(WeatherEntry.TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            validateCursor(contentValues, cursor);
        }
    }

    public static ContentValues getLocationContentValues(String testName, String locationSetting, double testLat, double testLng) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(LocationEntry.COLUMN_LOCATION_NAME, testName);
        contentValues.put(LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
        contentValues.put(LocationEntry.COLUMN_COORD_LAT, testLat);
        contentValues.put(LocationEntry.COLUMN_COORD_LONG, testLng);
        return contentValues;
    }

    public static ContentValues getWeatherContentValues(long locationRowId) {
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

    public static long persistContentValues(String tableName, ContentValues values, SQLiteDatabase db) {
        return db.insert(tableName, null, values);
    }

    public static void validateCursor(ContentValues values, Cursor cursor) {
        Set<Map.Entry<String, Object>> valueSet = values.valueSet();

        for(Map.Entry<String, Object> entry: valueSet) {
            String columnName = entry.getKey();
            int idx = cursor.getColumnIndex(columnName);

            assertFalse(-1 == idx);

            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, cursor.getString(idx));
        }
    }
}