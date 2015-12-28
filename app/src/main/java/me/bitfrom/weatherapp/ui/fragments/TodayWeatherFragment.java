package me.bitfrom.weatherapp.ui.fragments;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.Bind;
import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.ui.BaseFragment;

public class TodayWeatherFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @Bind(R.id.todays_container)
    protected LinearLayout linearLayout;

    @Override
    protected int getContentView() {
        return R.layout.todays_weather_fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
}
