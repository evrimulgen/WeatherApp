package me.bitfrom.weatherapp.ui.fragments;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.ui.BaseFragment;
import me.bitfrom.weatherapp.utils.NetworkStateChecker;

import static me.bitfrom.weatherapp.database.WeatherContract.WeatherEntry;

public class TodayWeatherFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @Bind(R.id.today_container)
    protected LinearLayout linearLayout;
    @Bind(R.id.today_city)
    protected TextView todayCity;
    @Bind(R.id.today_day_temp)
    protected TextView todayDayTemp;
    @Bind(R.id.today_max_temp)
    protected TextView todayMaxTemp;
    @Bind(R.id.today_min_temp)
    protected TextView todayMinTemp;
    @Bind(R.id.today_humidity)
    protected TextView todayHumidity;
    @Bind(R.id.today_wind_speed)
    protected TextView todayWindSpeed;
    @Bind(R.id.today_description)
    protected TextView todayDescription;

    private static final int TODAY_ITEM_LOADER = 0;

    @Override
    protected int getContentView() {
        return R.layout.today_weather_fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TODAY_ITEM_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStop() {
        dataLoaderWeapon.hideWeapon();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        getLoaderManager().destroyLoader(TODAY_ITEM_LOADER);
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = WeatherEntry.CONTENT_URI;
        baseUri = baseUri.buildUpon().appendQueryParameter("limit", "1").build();

        return new CursorLoader(getActivity(), baseUri, WeatherEntry.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        setWeatherInfo(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /***
     * Sets weather info from the Cursor into the views
     ***/
    private void setWeatherInfo(Cursor data) {
        if (data != null && data.moveToFirst()) {

            String dayTemp = data.getString(data.getColumnIndex(WeatherEntry.COLUMN_DAYTEMPERATURE)) + celsiusSign;
            String maxTemp = data.getString(data.getColumnIndex(WeatherEntry.COLUMN_MAXTEMPERATURE)) + celsiusSign;
            String minTemp = data.getString(data.getColumnIndex(WeatherEntry.COLUMN_MINTEMPERATURE)) + celsiusSign;
            String humidity = data.getString(data.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY)) + percentSymbol;
            String windSpeed = data.getString(data.getColumnIndex(WeatherEntry.COLUMN_WINDSPEED)) + metersPerSecond;

            todayCity.setText(data.getString(data.getColumnIndex(WeatherEntry.COLUMN_CITY)));
            todayDayTemp.setText(dayTemp);
            todayMaxTemp.setText(maxTemp);
            todayMinTemp.setText(minTemp);
            todayHumidity.setText(humidity);
            todayWindSpeed.setText(windSpeed);
            todayDescription.setText(data.getString(data.getColumnIndex(WeatherEntry.COLUMN_DESCRIPTION)));
        } else {
            loadData();
        }
    }

    /***
     * Loads weather data if the database is empty
     ***/
    private void loadData() {
        if (NetworkStateChecker.isNetworkAvailable(getActivity())) {
            dataLoaderWeapon.loadWeatherData();
        } else {
            messageHandlerUtility.showMessage(linearLayout, errorCheckNetwork, Snackbar.LENGTH_LONG);
        }
    }
}
