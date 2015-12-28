package me.bitfrom.weatherapp.ui.fragments;


import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.ui.BaseFragment;

public class WeeksWeatherFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected int getContentView() {
        return R.layout.weeks_forecast_fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataLoaderWeapon.loadWeatherData();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onStop() {
        super.onStop();
        dataLoaderWeapon.hideWeapon();
    }
}
