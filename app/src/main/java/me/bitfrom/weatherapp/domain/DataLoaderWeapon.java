package me.bitfrom.weatherapp.domain;

import javax.inject.Inject;

import me.bitfrom.weatherapp.BuildConfig;
import me.bitfrom.weatherapp.WeatherApplication;
import me.bitfrom.weatherapp.network.WeatherLoadService;
import me.bitfrom.weatherapp.network.models.WeatherModel;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class DataLoaderWeapon {

    @Inject
    protected WeatherLoadService loadService;
    @Inject
    protected DataSaverWeapon saverWeapon;

    private Subscription subscription;

    public DataLoaderWeapon() {
        WeatherApplication.appComponent().inject(this);
    }

    /***
     * Loads data from the server and passes it into DataSaverWeapons hands
     ***/
    public void loadWeatherData() {
        subscription = loadService.getWeather()
                .subscribeOn(Schedulers.io())
                .cache()
                .subscribe(new Observer<WeatherModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(WeatherModel weatherModel) {
                        saverWeapon.saveData(weatherModel);
                    }
                });
    }

    /***
     * Uses user location to load data from the server and passe it into DataSaverWeapons hands
     * @param lat users latitude
     * @param lon users longitude
     ***/
    public void loadWeatherDataUsingLocation(String lat, String lon) {
        subscription = loadService.getWeatherWithLocation(lat, lon)
                .subscribeOn(Schedulers.io())
                .cache()
                .subscribe(new Observer<WeatherModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(WeatherModel weatherModel) {
                        saverWeapon.saveData(weatherModel);
                    }
                });
    }

    public void hideWeapon() {
        if (subscription != null) subscription.unsubscribe();
    }
}
