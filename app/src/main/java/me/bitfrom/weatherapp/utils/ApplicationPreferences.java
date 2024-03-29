package me.bitfrom.weatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.bitfrom.weatherapp.constants.ConstantsManager;

/**
 * For managing application's preferences
 */
public class ApplicationPreferences {

    private SharedPreferences preferences;

    public ApplicationPreferences(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setFacebookToken(String fbToken) {
        preferences.edit().putString(ConstantsManager.FB_TOKEN, fbToken).apply();
    }

    public String getFacebookToken() {
        return preferences.getString(ConstantsManager.FB_TOKEN, "");
    }

    public void setUserCredentials(String credentials) {
        preferences.edit().putString(ConstantsManager.FACEBOOK_USER_NAME, credentials).apply();
    }

    public String getUserCredentials() {
        return preferences.getString(ConstantsManager.FACEBOOK_USER_NAME, "");
    }

    public void setUserEmail(String email) {
        preferences.edit().putString(ConstantsManager.FACEBOOK_USER_EMAIL, email).apply();
    }

    public String getUserEmail() {
        return preferences.getString(ConstantsManager.FACEBOOK_USER_EMAIL, "");
    }

    public void setUserPictureUrl(String imgUrl) {
        preferences.edit().putString(ConstantsManager.FACEBOOK_USER_PICTURE, imgUrl).apply();
    }

    public String getUserPictureUrl() {
        return preferences.getString(ConstantsManager.FACEBOOK_USER_PICTURE, "");
    }

    public void setLastKnownLatitude(double lat) {
        preferences.edit().putString(ConstantsManager.USER_LAT, String.valueOf(lat)).apply();
    }

    public String getLastKnownLatitude() {
        return preferences.getString(ConstantsManager.USER_LAT, "1");
    }

    public void setLastKnownLongitude(double lon) {
        preferences.edit().putString(ConstantsManager.USER_LON, String.valueOf(lon)).apply();
    }

    public String getLastKnownLongitude() {
        return preferences.getString(ConstantsManager.USER_LON, "1");
    }

}
