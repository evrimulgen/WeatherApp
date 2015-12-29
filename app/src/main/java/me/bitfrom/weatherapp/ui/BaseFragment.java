package me.bitfrom.weatherapp.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.WeatherApplication;
import me.bitfrom.weatherapp.domain.DataLoaderWeapon;
import me.bitfrom.weatherapp.network.WeatherLoadService;
import me.bitfrom.weatherapp.utils.ApplicationPreferences;
import me.bitfrom.weatherapp.utils.MessageHandlerUtility;

/**
 * Parent fragment
 */
abstract public class BaseFragment extends Fragment {

    @Inject
    protected ApplicationPreferences preferences;

    @Inject
    protected WeatherLoadService weatherLoadService;

    @Inject
    protected MessageHandlerUtility messageHandlerUtility;

    @Inject
    protected DataLoaderWeapon dataLoaderWeapon;

    @BindString(R.string.error_something_went_wrong)
    protected String errorSomethingWentWrong;
    @BindString(R.string.error_network_isnt_available)
    protected String errorCheckNetwork;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherApplication.appComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    abstract protected int getContentView();

    @Override
    public void onStop() {
        dataLoaderWeapon.hideWeapon();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Glide.get(getActivity()).clearMemory();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
