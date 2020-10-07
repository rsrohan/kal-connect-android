package com.kal.connect.customLibs.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kal.connect.utilities.AppPreferences;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        //For registration of token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //To displaying token on logcat
        Log.d("TOKEN: ", refreshedToken);
        AppPreferences.getInstance().setDeviceToken(refreshedToken);

    }

}