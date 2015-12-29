package me.bitfrom.weatherapp.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.ui.BaseFragment;

public class AboutFragment extends BaseFragment {

    @Bind(R.id.about_container)
    protected NestedScrollView nestedScrollView;

    @Bind(R.id.smile)
    protected TextView smile;

    @Override
    protected int getContentView() {
        return R.layout.about_fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String smileStr = "¯\\_(ツ)_/¯";

        smile.setText(smileStr);

        smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar = Snackbar.make(nestedScrollView,
                        R.string.about_developer_name, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.about_snack_okay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });
    }
}
