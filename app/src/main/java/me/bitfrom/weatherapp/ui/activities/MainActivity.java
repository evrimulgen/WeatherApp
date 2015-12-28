package me.bitfrom.weatherapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.ui.BaseActivity;
import me.bitfrom.weatherapp.ui.fragments.AboutFragment;
import me.bitfrom.weatherapp.ui.fragments.TodaysWeatherFragment;
import me.bitfrom.weatherapp.ui.fragments.WeeksWeatherFragment;
import me.bitfrom.weatherapp.utils.CircleTransform;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    protected DrawerLayout drawer;
    @Bind(R.id.nav_view)
    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        setupDrawer();

        checkIfUserLogged();
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
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            getFragmentManager().beginTransaction().replace(R.id.main_container,
                    new TodaysWeatherFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_gallery) {
            getFragmentManager().beginTransaction().replace(R.id.main_container,
                    new WeeksWeatherFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_slideshow) {
            getFragmentManager().beginTransaction().replace(R.id.main_container,
                    new AboutFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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

        TextView userCredentials = (TextView) header.findViewById(R.id.user_credentials);
        TextView userEmail = (TextView) header.findViewById(R.id.user_email);
        ImageView userAvatar = (ImageView) header.findViewById(R.id.user_avatar);

        userCredentials.setText(preferences.getUserCredentials());
        userEmail.setText(preferences.getUserEmail());

        Glide.with(this)
                .load(preferences.getUserPictureUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform(this))
                .into(userAvatar);
    }
}
