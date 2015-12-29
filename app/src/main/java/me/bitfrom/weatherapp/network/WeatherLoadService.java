package me.bitfrom.weatherapp.network;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.WeatherApplication;
import me.bitfrom.weatherapp.constants.ConstantsManager;
import me.bitfrom.weatherapp.network.models.WeatherModel;
import me.bitfrom.weatherapp.network.rest.OpenWeatherAPI;
import me.bitfrom.weatherapp.utils.ApplicationPreferences;
import rx.Observable;

/**
 * Provides all network-interacting methods
 **/
public class WeatherLoadService {

    @Inject
    protected OpenWeatherAPI openWeatherAPI;
    @Inject
    protected ApplicationPreferences preferences;
    @Inject
    protected Context context;

    public WeatherLoadService() {
        WeatherApplication.appComponent().inject(this);
    }

    public Observable<WeatherModel> getWeather() {
        SharedPreferences pref = preferences.getPreferences();
        String city = pref.getString(context.getString(R.string.pref_location_key),
                context.getString(R.string.pref_location_default));

        return openWeatherAPI.getWeather(city,
                ConstantsManager.DEFAULT_UNITS,
                ConstantsManager.DEFAULT_DAYS,
                ConstantsManager.OPEN_WEATHER_MAP_API_KEY);
    }

    public Observable<WeatherModel> getWeatherWithLocation(String lat, String lon) {
        return openWeatherAPI.getWeatherWithLocation(lat, lon,
                ConstantsManager.DEFAULT_UNITS, ConstantsManager.DEFAULT_DAYS,
                ConstantsManager.OPEN_WEATHER_MAP_API_KEY);
    }
}
