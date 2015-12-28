package me.bitfrom.weatherapp.ui.fragments;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.ui.BaseFragment;

import static me.bitfrom.weatherapp.database.WeatherContract.WeatherEntry;

public class WeeksWeatherFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.week_forecast_container)
    protected RelativeLayout relativeLayout;
    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.forecast_list_empty)
    protected TextView emptyView;
    @Bind(R.id.week_date_stamp)
    protected TextView dateStamp;
    @Bind(R.id.week_day_temp)
    protected TextView dayTemp;
    @Bind(R.id.week_max_temp)
    protected TextView maxTemp;
    @Bind(R.id.week_min_temp)
    protected TextView minTemp;
    @Bind(R.id.week_humidity)
    protected TextView humidity;
    @Bind(R.id.week_wind_speed)
    protected TextView windSpeed;
    @Bind(R.id.week_description)
    protected TextView description;

    private static final int FORECAST_LOADER = 1;

    @Override
    protected int getContentView() {
        return R.layout.weeks_forecast_fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        getLoaderManager().destroyLoader(FORECAST_LOADER);
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = WeatherEntry.CONTENT_URI;

        return new CursorLoader(getActivity(), baseUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
