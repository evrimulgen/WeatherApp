package me.bitfrom.weatherapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.BindString;
import me.bitfrom.weatherapp.BuildConfig;
import me.bitfrom.weatherapp.R;
import me.bitfrom.weatherapp.constants.ConstantsManager;
import me.bitfrom.weatherapp.ui.BaseActivity;
import me.bitfrom.weatherapp.utils.NetworkStateChecker;
import timber.log.Timber;

/**
 * User login Activity
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.activity_login_root_layout)
    protected LinearLayout linearLayout;
    @Bind(R.id.login_button)
    protected LoginButton loginButton;
    @BindString(R.string.activity_login_cancel_message)
    protected String cancelMessage;
    @BindString(R.string.error_network_isnt_available)
    protected String errorCheckNetwork;

    private CallbackManager mCallbackManager;

    @Override
    protected int getContentView() {
        return R.layout.login_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList(ConstantsManager.FACEBOOK_PERMISSION_EMAIL,
                ConstantsManager.FACEBOOK_PERMISSION_USER_FRIENDS));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkStateChecker.isNetworkAvailable(this)) {
            loginButton.registerCallback(mCallbackManager, callback);
        } else {
            messageHandlerUtility.showMessage(linearLayout, errorCheckNetwork, Snackbar.LENGTH_LONG);
        }
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            if (BuildConfig.DEBUG) {
                Timber.d(accessToken.getToken());
            }
            preferences.setFacebookToken(accessToken.getToken());
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject data,
                                GraphResponse response) {
                            if (response != null) {
                                try {
                                    if (data.has(ConstantsManager.FACEBOOK_USER_NAME)) {
                                        preferences.setUserCredentials(data.getString(ConstantsManager.FACEBOOK_USER_NAME));
                                    }
                                    if (data.has(ConstantsManager.FACEBOOK_USER_EMAIL)) {
                                        preferences.setUserEmail(data.getString(ConstantsManager.FACEBOOK_USER_EMAIL));
                                    }
                                    if (data.has(ConstantsManager.FACEBOOK_USER_PICTURE)) {
                                        preferences.setUserPictureUrl(data.getJSONObject(ConstantsManager.FACEBOOK_USER_PICTURE)
                                                .getJSONObject("data").getString("url"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                backToTheMainActivity();
                            } else {
                                messageHandlerUtility.showMessage(linearLayout,
                                        errorSomethingWentWrong, Snackbar.LENGTH_LONG);
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString(ConstantsManager.FACEBOOK_GRAPH_BUNDLE_KEY,
                    ConstantsManager.FACEBOOK_GRAPH_PATH);
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            messageHandlerUtility.showMessage(linearLayout, cancelMessage, Snackbar.LENGTH_LONG);
        }

        @Override
        public void onError(FacebookException error) {
            if (BuildConfig.DEBUG) {
                error.printStackTrace();
            }
            messageHandlerUtility.showMessage(linearLayout, errorSomethingWentWrong, Snackbar.LENGTH_LONG);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    /**
     * If successfully logged, go back to the MainActivity
     * **/
    private void backToTheMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
