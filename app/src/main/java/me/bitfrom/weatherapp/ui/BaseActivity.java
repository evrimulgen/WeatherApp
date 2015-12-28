package me.bitfrom.weatherapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.WeatherApplication;
import me.bitfrom.weatherapp.constants.ConstantsManager;
import me.bitfrom.weatherapp.utils.ApplicationPreferences;
import me.bitfrom.weatherapp.utils.MessageHandlerUtility;
import timber.log.Timber;

/**
 * Parent Activity
 */
abstract public class BaseActivity extends AppCompatActivity{

    @Inject
    protected ApplicationPreferences preferences;

    @Inject
    protected MessageHandlerUtility messageHandlerUtility;

    @BindString(R.string.error_something_went_wrong)
    protected String errorSomethingWentWrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentView());

        WeatherApplication.appComponent().inject(this);

        ButterKnife.bind(this);

        Timber.tag(ConstantsManager.APP_TAG);
    }

    abstract protected int getContentView();

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }
}
