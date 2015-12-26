package com.reiyu.sleepflower;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.facebook.FacebookSdk;

/**
 * Created by Satomi on 12/26/15.
 */
public class FacebookActivity extends FragmentActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
    }
}

