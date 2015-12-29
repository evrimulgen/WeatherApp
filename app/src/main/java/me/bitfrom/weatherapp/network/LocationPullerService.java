package me.bitfrom.weatherapp.network;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import javax.inject.Inject;

import me.bitfrom.weatherapp.BuildConfig;
import me.bitfrom.weatherapp.WeatherApplication;
import me.bitfrom.weatherapp.utils.ApplicationPreferences;
import timber.log.Timber;

public class LocationPullerService extends Service {

    @Inject
    protected ApplicationPreferences preferences;

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 86400000;
    private static final float LOCATION_DISTANCE = 10000f;

    public LocationPullerService() {
        super();
        WeatherApplication.appComponent().inject(this);
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            if (BuildConfig.DEBUG) Timber.d("LocationListener " + provider);
            mLastLocation = new Location(provider);
            preferences.setLastKnownLatitude(mLastLocation.getLatitude());
            preferences.setLastKnownLongitude(mLastLocation.getLongitude());
        }

        @Override
        public void onLocationChanged(Location location) {
            if (BuildConfig.DEBUG) Timber.d("onLocationChanged: " + location);
            mLastLocation.set(location);
            preferences.setLastKnownLatitude(location.getLatitude());
            preferences.setLastKnownLongitude(location.getLongitude());
        }

        @Override
        public void onProviderDisabled(String provider) {
            if (BuildConfig.DEBUG) Timber.d("onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (BuildConfig.DEBUG) Timber.d("onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (BuildConfig.DEBUG) Timber.d("onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (BuildConfig.DEBUG) Timber.d("onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) Timber.d("onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            if (BuildConfig.DEBUG) Timber.d("fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            if (BuildConfig.DEBUG) Timber.d("network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            if (BuildConfig.DEBUG) Timber.d("fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            if (BuildConfig.DEBUG) Timber.d("gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        if (BuildConfig.DEBUG) Timber.d("onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    if (BuildConfig.DEBUG) Timber.d("fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        if (BuildConfig.DEBUG) Timber.d("initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
