package me.bitfrom.weatherapp.config;

import android.app.Application;
import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.bitfrom.weatherapp.BuildConfig;
import me.bitfrom.weatherapp.constants.RequestsKeeper;
import me.bitfrom.weatherapp.domain.DataLoaderWeapon;
import me.bitfrom.weatherapp.domain.DataSaverWeapon;
import me.bitfrom.weatherapp.domain.NotificationWeapon;
import me.bitfrom.weatherapp.network.WeatherLoadService;
import me.bitfrom.weatherapp.network.rest.OpenWeatherAPI;
import me.bitfrom.weatherapp.utils.ApplicationPreferences;
import me.bitfrom.weatherapp.utils.MessageHandlerUtility;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Using Dagger for DI all components
 */
@Module
@SuppressWarnings({"unused", "deprecation"})
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context providesContext() {
        return application;
    }

    @Provides
    @Singleton
    public ApplicationPreferences providesApplicationPreferences(Context context) {
        return new ApplicationPreferences(context);
    }

    @Provides
    @Singleton
    public MessageHandlerUtility providesMessageHandlerUtility() {
        return new MessageHandlerUtility();
    }

    @Provides
    @Singleton
    public OpenWeatherAPI providesRestService() {

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .disableHtmlEscaping()
                .create();

        OkHttpClient client = new OkHttpClient();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RequestsKeeper.END_POINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit.create(OpenWeatherAPI.class);
    }

    @Provides
    @Singleton
    public WeatherLoadService providesWeatherLoadService() {
        return new WeatherLoadService();
    }

    @Provides
    @Singleton
    public DataSaverWeapon providesDataSaverWeapon() {
        return new DataSaverWeapon();
    }

    @Provides
    @Singleton
    public DataLoaderWeapon providesDataLoaderWeapon() {
        return new DataLoaderWeapon();
    }

    @Provides
    @Singleton
    public NotificationWeapon providesNotificationWeapon() {
        return new NotificationWeapon();
    }
}
