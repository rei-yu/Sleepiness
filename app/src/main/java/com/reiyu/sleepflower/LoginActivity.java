package com.reiyu.sleepflower;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

/**
 * Created by Satomi on 12/26/15.
 */
public class LoginActivity extends FragmentActivity {
    private final String LOG_TAG = "Facebook Login";
    CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.fragment_facebook);

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e(LOG_TAG, "on succeed");
                        String accessToken = loginResult.getAccessToken().getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivity", response.toString());
                                // Get facebook data from login
                                Bundle bFacebookData = getFacebookData(object);
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.e(LOG_TAG, "canceled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e(LOG_TAG, "error");
                    }
                }

        );
    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

//            try {
//                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
//                Log.i("profile_pic", profile_pic + "");
//                bundle.putString("profile_pic", profile_pic.toString());
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                return null;
//            }

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

            sp.edit().putString("@string/uid", id).commit();
            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                sp.edit().putString("@string/first_name", object.getString("first_name")).commit();
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                sp.edit().putString("@string/last_name", object.getString("last_name")).commit();
                bundle.putString("last_name", object.getString("last_name"));
            return bundle;
        } catch (Exception e) {
            Log.e(LOG_TAG, "JSON Parse Error", e);
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putBoolean("@string/sign_in", true).commit();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}
