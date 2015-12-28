package me.bitfrom.weatherapp.domain;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import me.bitfrom.weatherapp.BuildConfig;
import me.bitfrom.weatherapp.WeatherApplication;
import me.bitfrom.weatherapp.network.models.WeatherModel;
import timber.log.Timber;

import static me.bitfrom.weatherapp.database.WeatherContract.WeatherEntry;

public class DataSaverWeapon {

    @Inject
    protected Context context;

    public DataSaverWeapon() {
        WeatherApplication.appComponent().inject(this);
    }

    /***
     * Saves data using ContentProviders power)
     ***/
    public void saveData(WeatherModel weatherModel) {
        ArrayList<ContentValues> cvList = new ArrayList<>(weatherModel.getList().size());

        //We don't want to use much memory for our dirty work,
        // so using simple for loop helps us to avoid memory allocations
        int listSize = weatherModel.getList().size();

        GregorianCalendar calendar = new GregorianCalendar();
        long timeInMillis = calendar.getTimeInMillis();

        for (int i = 0; i < listSize; i++) {
            ContentValues weatherValues = new ContentValues();

            weatherValues.put(WeatherEntry.COLUMN_DATESTAMP, weatherModel.getList().get(i).getDt());
            weatherValues.put(WeatherEntry.COLUMN_CITY, weatherModel.getCity().getName());
            weatherValues.put(WeatherEntry.COLUMN_DAYTEMPERATURE, weatherModel.getList().get(i).getTemp().getDay());
            weatherValues.put(WeatherEntry.COLUMN_MAXTEMPERATURE, weatherModel.getList().get(i).getTemp().getMax());
            weatherValues.put(WeatherEntry.COLUMN_MINTEMPERATURE, weatherModel.getList().get(i).getTemp().getMin());
            weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, weatherModel.getList().get(i).getHumidity());
            for (int j = 0; j < weatherModel.getList().get(i).getWeather().size(); j++) {
                weatherValues.put(WeatherEntry.COLUMN_DESCRIPTION, weatherModel.getList().get(i).getWeather().get(j).getDescription());
            }
            weatherValues.put(WeatherEntry.COLUMN_WINDSPEED, weatherModel.getList().get(i).getSpeed());
            weatherValues.put(WeatherEntry.COLUMN_CURRENT_DATESTAMP, timeInMillis);

            cvList.add(weatherValues);
        }

        if (cvList.size() > 0) {
            if (BuildConfig.DEBUG) {
                Timber.d(cvList + " ");
            }
            ContentValues[] cvArray = new ContentValues[cvList.size()];
            cvList.toArray(cvArray);

            context.getContentResolver()
                    .bulkInsert(WeatherEntry.CONTENT_URI, cvArray);

            //Clearing the old data
            int deleted = context.getContentResolver()
                    .delete(WeatherEntry.CONTENT_URI,
                            WeatherEntry.COLUMN_CURRENT_DATESTAMP + " < ? ",
                            new String[]{Long.toString(timeInMillis)});

            if (BuildConfig.DEBUG) {
                Timber.d(deleted + " values deleted.");
                Timber.d(cvList.size() + " values inserted.");
            }
            //No need after inserting values into the database
            cvList.clear();
        }
    }
}
