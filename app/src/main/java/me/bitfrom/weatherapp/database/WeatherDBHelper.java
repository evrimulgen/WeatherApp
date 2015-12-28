package me.bitfrom.weatherapp.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static me.bitfrom.weatherapp.database.WeatherContract.*;

public class WeatherDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "weather.db";

    public WeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateWeatherTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + WeatherEntry.TABLE_NAME);
        onCreate(db);
    }

    private static String sqlCreateWeatherTable() {
        return "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
                WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +

                WeatherEntry.COLUMN_DATESTAMP + " TEXT NOT NULL, " +
                WeatherEntry.COLUMN_DAYTEMPERATURE + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_MAXTEMPERATURE + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_MINTEMPERATURE + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_HUMIDITY + " INTEGER NOT NULL, " +
                WeatherEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                WeatherEntry.COLUMN_WINDSPEED + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_CURRENT_DATESTAMP + " TEXT NOT NULL" + " );";
    }
}
