package me.bitfrom.weatherapp.utils;

import android.app.IntentService;
import android.content.Intent;

import com.bumptech.glide.Glide;

public class ServiceCacheCleaner extends IntentService{

    public ServiceCacheCleaner() {
        super("ServiceThatCleansGlideDiskCache");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Glide.get(this).clearDiskCache();
    }
}
