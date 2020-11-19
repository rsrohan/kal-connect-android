package com.kal.connect.customLibs.fcm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.kal.connect.utilities.AppPreferences;


public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        // Save token
        //To displaying token on logcat
        Log.d("TOKEN: ", s);
        AppPreferences.getInstance().setDeviceToken(s);
    }
}
