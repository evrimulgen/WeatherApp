package me.bitfrom.weatherapp.ui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;

import butterknife.Bind;
import butterknife.BindString;
import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.ui.BaseActivity;
import me.bitfrom.weatherapp.ui.fragments.AboutFragment;
import me.bitfrom.weatherapp.ui.fragments.SettingsFragment;
import me.bitfrom.weatherapp.ui.fragments.TodayWeatherFragment;
import me.bitfrom.weatherapp.ui.fragments.WeeksWeatherFragment;
import me.bitfrom.weatherapp.utils.CircleTransform;
import me.bitfrom.weatherapp.utils.ServiceCacheCleaner;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    protected DrawerLayout drawer;
    @Bind(R.id.nav_view)
    protected NavigationView navigationView;
    @BindString(R.string.dialog_logout_message)
    protected String logoutMessage;
    @BindString(R.string.dialog_logout_yes)
    protected String logoutYes;
    @BindString(R.string.dialog_logout_no)
    protected String logoutNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        setupDrawer();

        //startService(new Intent(this, LocationPullerService.class));

        checkIfUserLogged();

        if (savedInstanceState == null) {
            replaceFragment(new TodayWeatherFragment());
        }

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment f = getFragmentManager().findFragmentById(R.id.main_container);
                if (f != null) {
                    updateToolbarTitle(f);
                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_weeks_forecast:
                WeeksWeatherFragment wwf = new WeeksWeatherFragment();
                replaceFragment(wwf);
                break;
            case R.id.nav_about:
                AboutFragment af = new AboutFragment();
                replaceFragment(af);
                break;
            case R.id.nav_settings:
                SettingsFragment sf = new SettingsFragment();
                replaceFragment(sf);
                break;
            case R.id.nav_logout:
                logout();
                break;
            default:
                TodayWeatherFragment twf = new TodayWeatherFragment();
                replaceFragment(twf);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /***
     * Setup NavigationDrawer
     ***/
    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        loadUserInfo(navigationView);
    }

    /***
     * For managing fragments transaction
     ***/
    private void replaceFragment(Fragment fragment) {
        String backStateName =  fragment.getClass().getName();

        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null){
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_container, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    /***
     * Updates toolbar title
     ***/
    private void updateToolbarTitle(Fragment fragment){
        String fragmentClassName = fragment.getClass().getName();

        if (fragmentClassName.equals(TodayWeatherFragment.class.getName())) {
            setTitle(getString(R.string.today_weather_fragment_title));
            navigationView.setCheckedItem(R.id.nav_todays_forecast);
        } else if (fragmentClassName.equals(WeeksWeatherFragment.class.getName())) {
            setTitle(getString(R.string.weeks_forecast_fragment_title));
            navigationView.setCheckedItem(R.id.nav_weeks_forecast);
        } else if (fragmentClassName.equals(AboutFragment.class.getName())) {
            setTitle(getString(R.string.about_fragment_title));
            navigationView.setCheckedItem(R.id.nav_about);
        } else if (fragmentClassName.equals(SettingsFragment.class.getName())) {
            setTitle(getString(R.string.settings_fragment_title));
            navigationView.setCheckedItem(R.id.nav_settings);
        }
    }

    /***
     * If user isn't logged starts LoginActivity
     ***/
    private void checkIfUserLogged() {
        if (preferences.getFacebookToken().equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    /***
     * Loads user info into the navigation header
     ***/
    private void loadUserInfo(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        //Find
        TextView userCredentials = (TextView) header.findViewById(R.id.user_credentials);
        TextView userEmail = (TextView) header.findViewById(R.id.user_email);
        ImageView userAvatar = (ImageView) header.findViewById(R.id.user_avatar);
        //Set
        userCredentials.setText(preferences.getUserCredentials());
        userEmail.setText(preferences.getUserEmail());
        //Load
        Glide.with(this)
                .load(preferences.getUserPictureUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform(this))
                .into(userAvatar);
    }

    private void logout() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(preferences.getUserCredentials() + logoutMessage);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                logoutYes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startService(new Intent(getBaseContext(), ServiceCacheCleaner.class));
                        Glide.get(getBaseContext()).clearMemory();
                        preferences.setFacebookToken("");
                        preferences.setUserCredentials("");
                        preferences.setUserEmail("");
                        preferences.setUserPictureUrl("");
                        LoginManager.getInstance().logOut();
                        startActivity(new Intent(getBaseContext(), LoginActivity.class));
                        finish();
                    }
                });

        builder1.setNegativeButton(
                logoutNo,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
