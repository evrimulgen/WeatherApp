package me.bitfrom.weatherapp.config;

import javax.inject.Singleton;

import dagger.Component;
import me.bitfrom.weatherapp.WeatherApplication;
import me.bitfrom.weatherapp.domain.DataLoaderWeapon;
import me.bitfrom.weatherapp.domain.DataSaverWeapon;
import me.bitfrom.weatherapp.network.WeatherLoadService;
import me.bitfrom.weatherapp.ui.BaseActivity;
import me.bitfrom.weatherapp.ui.BaseFragment;

/**
 * Dagger AppComponent interface
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(WeatherApplication weatherApplication);

    void inject(BaseActivity baseActivity);

    void inject(BaseFragment baseFragment);

    void inject(WeatherLoadService weatherLoadService);

    void inject(DataSaverWeapon dataSaverWeapon);

    void inject(DataLoaderWeapon dataLoaderWeapon);
}
