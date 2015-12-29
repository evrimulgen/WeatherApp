package me.bitfrom.weatherapp.network.rest;

import me.bitfrom.weatherapp.constants.RequestsKeeper;
import me.bitfrom.weatherapp.network.models.WeatherModel;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Contains all request methods
 **/
public interface OpenWeatherAPI {

    @GET(RequestsKeeper.DAILY)
    Observable<WeatherModel> getWeather(@Query("q") String city,
                                        @Query("units") String units,
                                        @Query("cnt") String days,
                                        @Query("appid") String appId);

    @GET(RequestsKeeper.DAILY)
    Observable<WeatherModel> getWeatherWithLocation(@Query("lat") String lat,
                                                    @Query("lon") String lon,
                                                    @Query("units") String units,
                                                    @Query("cnt") String days,
                                                    @Query("appid") String appId);
}
