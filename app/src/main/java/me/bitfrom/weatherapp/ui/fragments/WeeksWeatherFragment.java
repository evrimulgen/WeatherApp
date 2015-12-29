package me.bitfrom.weatherapp.ui.fragments;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.BindString;
import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.ui.BaseFragment;
import me.bitfrom.weatherapp.ui.recyclerview.DividerItemDecoration;
import me.bitfrom.weatherapp.ui.recyclerview.EmptyRecyclerView;
import me.bitfrom.weatherapp.ui.recyclerview.ForecastRVAdapter;
import me.bitfrom.weatherapp.ui.recyclerview.RecyclerItemClickListener;
import me.bitfrom.weatherapp.utils.NetworkStateChecker;
import me.bitfrom.weatherapp.utils.ShareUtility;

import static me.bitfrom.weatherapp.database.WeatherContract.WeatherEntry;

public class WeeksWeatherFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.week_forecast_container)
    protected RelativeLayout relativeLayout;
    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.weather_forecast)
    protected EmptyRecyclerView weatherForecast;
    @Bind(R.id.forecast_list_empty)
    protected TextView emptyView;
    @BindString(R.string.share_with_title)
    protected String shareWithTitle;

    private static final int FORECAST_LOADER = 1;
    private ForecastRVAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        initSwipeToRefresh();
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
        recyclerViewAdapter.swapCursor(data);
        updateEmptyView();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /***
     * Init our RecyclerView and set some decorations on it
     ***/
    private void initRecyclerView() {
        weatherForecast.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        layoutManager = new LinearLayoutManager(getActivity());
        weatherForecast.setLayoutManager(layoutManager);
        weatherForecast.setItemAnimator(new DefaultItemAnimator());

        recyclerViewAdapter = new ForecastRVAdapter(getActivity(), null, 0);
        weatherForecast.setEmptyView(emptyView);
        weatherForecast.setAdapter(recyclerViewAdapter);

        weatherForecast.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Uri uri = WeatherEntry.buildHamstersUri(recyclerViewAdapter.getItemId(position));
                //I know it's a bad thing, but ContentProvider is too old to deal with async((
                Cursor cursor = getActivity().getContentResolver().query(uri, WeatherEntry.SHARE_PROJECTION, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String date = cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_DATESTAMP));
                    String dayTemp = cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_DAYTEMPERATURE));
                    String description = cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_DESCRIPTION));
                    ShareUtility.getShareActions(getActivity(), date, dayTemp, description)
                            .title(shareWithTitle).show();
                    cursor.close();
                }
            }
        }));
    }

    /***
     * Updates our weather forecast using swipe to refresh mechanism
     ***/
    private void initSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStateChecker.isNetworkAvailable(getActivity())) {
                    //The lat and lon not setted, use "default" location
                    if (preferences.getLastKnownLatitude().equalsIgnoreCase("1")) {
                        dataLoaderWeapon.loadWeatherData();
                    } else {
                        dataLoaderWeapon.loadWeatherDataUsingLocation(preferences.getLastKnownLatitude(),
                                preferences.getLastKnownLongitude());
                    }
                } else {
                    messageHandlerUtility.showMessage(relativeLayout, errorCheckNetwork,
                            Snackbar.LENGTH_LONG);
                }
            }
        });
    }

    /***
     * If forecast list list is empty, show a message.
     ***/
    private void updateEmptyView() {
        if (recyclerViewAdapter.getCount() == 0) {
            TextView emptyView = (TextView) getView().findViewById(R.id.forecast_list_empty);
            if (null != emptyView) {
                String message = getString(R.string.empty_forecast_list);
                if (! NetworkStateChecker.isNetworkAvailable(getActivity())) {
                    message = getString(R.string.error_network_isnt_available);
                }
                emptyView.setText(message);
            }
        }
    }
}
