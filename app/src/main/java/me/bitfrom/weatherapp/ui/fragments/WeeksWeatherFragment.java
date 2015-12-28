package me.bitfrom.weatherapp.ui.fragments;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.ui.BaseFragment;
import me.bitfrom.weatherapp.ui.recyclerview.DividerItemDecoration;
import me.bitfrom.weatherapp.ui.recyclerview.EmptyRecyclerView;
import me.bitfrom.weatherapp.ui.recyclerview.ForecastRVAdapter;
import me.bitfrom.weatherapp.utils.NetworkStateChecker;

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
    }

    /**
     * If hamsters list is empty, show a message.
     * **/
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
