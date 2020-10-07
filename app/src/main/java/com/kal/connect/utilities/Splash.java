package com.kal.connect.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.fcm.OnClearFromRecentService;
import com.kal.connect.modules.dashboard.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.HashMap;

public class Splash extends AppCompatActivity {

    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
//        getVideoCallConfigurations();
        AppPreferences.localaizationStatus = true;
        Utilities.updateLanguageSettings(Splash.this);

        Utilities.setAppLocale(this);
        txtTitle.setText(R.string.app_name); //tv1 is textview in my activity

        AppPreferences.getInstance().setIsAppKilled(false);
        createNotificationChannel();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
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

                /* Create an Intent that will start the Menu-Activity. */

// Show Dashboard directly without login
//                Intent homeScreen = new Intent(getApplicationContext(), SignIn.class);
//                homeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(homeScreen);


// Perform Login before showing dashboard
//                String hardcodedUser = "{'PatientID':32398,'FirstName':'Mohan','LastName':'K','DOB':'1995-08-17T18:37:52','Sex':'3','Email':'info@mm.com','ContactNo':'9791841777','Address':null,'CityID':470,'CreatedBy':8,'CreatedDate':'2019-08-18T00:07:03','IsActive':null,'PatientFullID':null,'voterID':null,'rationID':null,'Zipcode':'110002','Panno':null,'Addressline1':'test','Addressline2':'NGR','ClientID':12,'CreaterId':null,'PatHospId':null,'OPNumber':null,'Unit':null,'Ward':null,'OPProcedure':null,'ClientLogo':814,'CareTaker':'Caretaker','Image':'32398.jpg','MaritalStatus':'Single','PatBloodGrp':'A-','isProfileMoved':1,'Lattitude':'','Longitude':'','LocationAddress':'Golden Lake View Apartment, 4th Main Rd, NGR Layout, Roopena Agrahara, Bommanahalli, Bengaluru, Karnataka 560068, India, lat/lng: (12.9098736,77.62194199999999)','cityname':'Karur','city':470,'statename':'Tamil Nadu','stateid':31,'countryname':'India','Countryid':1,'password':'123456','APIStatus':'1'}";
//                AppPreferences.getInstance().setLoginInfo(hardcodedUser);


                if (AppPreferences.getInstance().checkLogin(Splash.this)) {
                    Intent homeScreen = new Intent(getApplicationContext(), Dashboard.class);
                    homeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(homeScreen);
                }
//
//                Splash.this.finish();

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

    public void getVideoCallConfigurations() {
//        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();
//        inputParams.put("ComplaintID",GlobValues.getInstance().getSelectedAppointment());
        HashMap<String, Object> endCallParams = new HashMap<>();
        endCallParams.put("User_id", "40915");
        SoapAPIManager apiManager = new SoapAPIManager(Splash.this, endCallParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);


            }
        }, true);
        String[] url = {Config.WEB_Services4, Config.END_CALL, "POST"};

        if (Utilities.isNetworkAvailable(Splash.this)) {
            apiManager.execute(url);
        } else {

        }
    }


}
