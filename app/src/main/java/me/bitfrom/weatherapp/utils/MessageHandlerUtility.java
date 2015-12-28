package me.bitfrom.weatherapp.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import me.bitfrom.weatherapp.R;

/**
 * Handle all visible for user messages
 **/
public class MessageHandlerUtility {

    public void showMessage(View view, String message, int snackBarLength) {
        switch (snackBarLength) {
            case Snackbar.LENGTH_LONG: {
                final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.action_dismiss_snackbar, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                break;
            }
            case Snackbar.LENGTH_SHORT: {
                final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
                snackbar.setAction(R.string.action_dismiss_snackbar, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                break;
            }
            default:
                break;
        }
    }
}
