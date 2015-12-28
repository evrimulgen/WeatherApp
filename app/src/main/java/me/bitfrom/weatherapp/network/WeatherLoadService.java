package me.bitfrom.weatherapp.network;

import javax.inject.Inject;

import me.bitfrom.weatherapp.WeatherApplication;
import me.bitfrom.weatherapp.constants.ConstantsManager;
import me.bitfrom.weatherapp.network.models.WeatherModel;
import me.bitfrom.weatherapp.network.rest.OpenWeatherAPI;
import rx.Observable;

/**
 * Provides all network-interacting methods
 **/
public class WeatherLoadService {

    @Inject
    protected OpenWeatherAPI openWeatherAPI;

    public WeatherLoadService() {
        WeatherApplication.appComponent().inject(this);
    }

    public Observable<WeatherModel> getWeather() {
        return openWeatherAPI.getWeather(ConstantsManager.DEFAULT_CITY,
                ConstantsManager.DEFAULT_UNITS,
                ConstantsManager.DEFAULT_DAYS,
                ConstantsManager.OPEN_WEATHER_MAP_API_KEY);
    }
}
