package com.kal.connect.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.kal.connect.R;
import com.kal.connect.customLibs.fcm.OnClearFromRecentService;
import com.kal.connect.modules.dashboard.DashboardMapActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    TextView txtTitle;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private static final String WELCOME_MESSAGE_KEY = "welcome_message";
    private FirebaseCrashlytics crashlytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        txtTitle = (TextView) findViewById(R.id.txtTitle);



        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);


        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            String welcomeMessage = mFirebaseRemoteConfig.getString(WELCOME_MESSAGE_KEY);

                            Config.minVersionCode = Integer.parseInt(mFirebaseRemoteConfig.getString("versionCode"));

                            System.out.println("Config.minVersionCode: " + Config.minVersionCode);
                            System.out.println("Config params welcomeMessage: " + welcomeMessage);

                        } else {
                            System.out.println("task Details"+task.getException());
                        }
                    }
                });




        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        AppPreferences.localaizationStatus = true;
        Utilities.updateLanguageSettings(SplashActivity.this);

        Utilities.setAppLocale(this);
        txtTitle.setText(R.string.app_name); //tv1 is textview in my activity

        AppPreferences.getInstance().setIsAppKilled(false);
        createNotificationChannel();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (!task.isSuccessful()) {
                            System.out.println(""+task.getException());
                            Log.e("tokenError", "onComplete: "+task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        System.out.println("Token" + token);
                        AppPreferences.getInstance().setDeviceToken(token);

                    }
                });


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (AppPreferences.getInstance().checkLogin(SplashActivity.this)) {
                    Intent homeScreen = new Intent(getApplicationContext(), DashboardMapActivity.class);
                    homeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(homeScreen);
                }
                SplashActivity.this.finish();

            }
        }, 2000);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "VideoCall";
            String description = "Remainder for Appointment";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(name + "", name, importance);
//            NotificationChannel channel = new NotificationChannel("Remainder-ID", name, importance);

            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }





}
