package me.bitfrom.weatherapp;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import me.bitfrom.weatherapp.config.AppComponent;
import me.bitfrom.weatherapp.config.AppModule;
import me.bitfrom.weatherapp.config.DaggerAppComponent;
import timber.log.Timber;

public class WeatherApplication extends Application {

    private static Context context;

    private AppComponent appComponent;

    public static AppComponent appComponent() {
        return ((WeatherApplication) getContext().getApplicationContext()).appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setContext(this);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);

        FacebookSdk.sdkInitialize(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            printHashKey();
        }
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        WeatherApplication.context = context;
    }

    private void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "me.bitfrom.weatherapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(signature.toByteArray());
                Timber.tag("Facebook hash");
                Timber.d(Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
