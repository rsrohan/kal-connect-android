package com.kal.connect.modules.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kal.connect.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.Maps.Manager.CustomMapActivity;
import com.kal.connect.modules.dashboard.tabs.Account.Account;
import com.kal.connect.modules.dashboard.tabs.Appointments.Appointments;
import com.kal.connect.modules.dashboard.tabs.Home.Home;
import com.kal.connect.modules.dashboard.tabs.Medicine.Medicine;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;
import com.kal.connect.utilities.UtilitiesInterfaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class Dashboard extends CustomMapActivity implements View.OnClickListener{

    // MARK : Properties
    BottomNavigationView bottomTab = null;

    Home homeFragment;
    Appointments appointmentFragment;



    // MARK : Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        buildUI();
    }

    // MARK : UIActions
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    // MARK : Instance Methods
    private void buildUI() {

        buildBottomTabs();
        getStateCityList();

        try{
            if(GlobValues.getInstance().getAddAppointmentParams() != null)
                GlobValues.getInstance().getAddAppointmentParams().clear();
        }catch (Exception e){

        }


        if(getIntent().hasExtra("FromNotification") && getIntent().getBooleanExtra("FromNotification",false))
        {
            loadFragment(new Appointments());
            bottomTab.setSelectedItemId(R.id.tab_appointments);

        }else{
            if(homeFragment == null){
                homeFragment = new Home();
            }
            loadFragment(homeFragment);
        }


        ImageButton sos = (ImageButton) findViewById(R.id.sos);
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(Dashboard.this)
                        .withPermissions(
                                Manifest.permission.CALL_PHONE

                        ).withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "7575005555"));
                        startActivity(intent);
                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();

            }
        });


//        try{
//            JSONObject userInfo = AppPreferences.getInstance().getUserInfo();
//            if(userInfo.has("Lattitude") && userInfo.get("Lattitude") != null && !userInfo.getString("Lattitude").isEmpty()  && !userInfo.getString("Lattitude").equals("null") &&
//                    userInfo.has("Longitude") && userInfo.get("Longitude") != null && !userInfo.getString("Longitude").isEmpty()  && !userInfo.getString("Longitude").equals("null")){
//
//            }else{
//                if(AppPreferences.getInstance().isLocationRequested())
//                    return;
//                Utilities.showAlertDialogWithOptions(this, getResources().getString(R.string.alert_map_options), new String[]{getResources().getString(R.string.btn_yes), getResources().getString(R.string.cancel)}, new UtilitiesInterfaces.AlertCallback() {
//                    @Override
//                    public void onOptionClick(DialogInterface dialog, int buttonIndex) {
//                        if(buttonIndex == 0)
//                        {
//                            launchPlacePicker();
//                        }
//                        if(buttonIndex == 1)
//                        {
//
//                        }
//                    }
//                });
//
//            }
//        }catch (Exception e){
//
//        }


    }

    // MARK : Bottom Tab bar
    private void buildBottomTabs() {

        // Build Tab bar with bottom navigation view
        bottomTab = (BottomNavigationView) findViewById(R.id.tab_bar);

        bottomTab.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment pageToShow = null;

                // Replace fragment for the selected tab
                switch (item.getItemId()) {

                    case R.id.tab_home:
                        if(homeFragment == null){
                            homeFragment = new Home();
                        }
                        pageToShow = homeFragment;
                        break;

                    case R.id.tab_appointments:
                        if(appointmentFragment == null){
                            appointmentFragment = new Appointments();
                        }
                        pageToShow = appointmentFragment;
                        break;

                    case R.id.tab_medicine:
                        pageToShow = new Medicine();

                        break;

                    case R.id.tab_account:
                        pageToShow = new Account();
                        break;

                }

                // load selected page
                if(pageToShow != null) {
                    loadFragment(pageToShow);
                }

                return pageToShow != null;

            }
        });

    }

    // Bottom Tab bar - Show selected Fragment
    public void loadFragment(Fragment pageToShow) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, pageToShow).commit();
    }

    // Add Badge with item id
    public static void showBadge(Context context, BottomNavigationView bottomNavigationView, int itemId, String value) {

        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.tab_badge, bottomNavigationView, false);

        TextView txtBadge = badge.findViewById(R.id.badge_text_view);
        txtBadge.setText(value);
        itemView.addView(badge);

    }

    // Remove Badge with item id
    public static void removeBadge(BottomNavigationView bottomNavigationView, int itemId) {

        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }

    }

    @Override
    public void onBackPressed() {
        Utilities.showAlertDialogWithOptions(Dashboard.this, getResources().getString(R.string.alert_exit), new String[]{getResources().getString(R.string.btn_yes), getResources().getString(R.string.cancel)}, new UtilitiesInterfaces.AlertCallback() {
            @Override
            public void onOptionClick(DialogInterface dialog, int buttonIndex) {
                if(buttonIndex == 0)
                {
                    finish();
                }
            }
        });
    }

    // MARK : Choose your location via location picker
    public void launchPlacePicker() {

        showPlacePicker(new CustomMapActivity.PlacePickCallback() {
            @Override
            public void receiveSelectedPlace(Boolean status, Place selectedPlace) {

                String addressInfo = "";
                if (status) {

                    if (selectedPlace != null){

                        // Name
                        addressInfo = (selectedPlace.getName() != null && selectedPlace.getName().length() > 0)? selectedPlace.getName().toString() : addressInfo;

                        // Address
                        addressInfo = (selectedPlace.getAddress() != null && selectedPlace.getAddress().length() > 0)? addressInfo + ", " + selectedPlace.getAddress().toString() : addressInfo;

                        // Phone
//                        addressInfo = (selectedPlace.getPhoneNumber() != null && selectedPlace.getPhoneNumber().length() > 0)? addressInfo + ", Phone: " + selectedPlace.getPhoneNumber().toString() : addressInfo;

                        // Location
                        addressInfo = (selectedPlace.getLatLng() != null)? addressInfo + ", " + selectedPlace.getLatLng() : addressInfo;
                        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

                        inputParams.put("Lattitude",""+selectedPlace.getLatLng().latitude);
                        inputParams.put("Longitude",""+selectedPlace.getLatLng().longitude);
                        inputParams.put("LocationAddress",addressInfo);
                        Utilities.updateLocation(Dashboard.this,inputParams);

                    }

                }

                // Set the address details
                if(addressInfo.length() > 0){

//                    addressTxtVw.setVisibility(View.VISIBLE);
//                    addressTxtVw.setText(addressInfo);
//                    techRequired.setSelected(true);

                }
            }
        });
    }

//    void updateLocation(HashMap<String, Object> inputParams){
//
//        SoapAPIManager apiManager = new SoapAPIManager(Dashboard.this, inputParams, new APICallback() {
//            @Override
//            public void responseCallback(Context context, String response) throws JSONException {
//                Log.e("***response***",response);
//
//                try{
//                    JSONArray responseAry = new JSONArray(response);
//                    if(responseAry.length()>0){
//                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
//                        if(commonDataInfo.getString("APIStatus").equals("1")){
//                            JSONObject userInfo = AppPreferences.getInstance().getUserInfo();
//                            userInfo.put("Lattitude",inputParams.get("Lattitude").toString());
//                            userInfo.put("Longitude",inputParams.get("Longitude").toString());
//                            userInfo.put("LocationAddress",inputParams.get("LocationAddress").toString());
//                            AppPreferences.getInstance().setLoginInfo(userInfo.toString());
//                        }
//                    }
//                }catch (Exception e){
//
//                }
//            }
//        },true);
//        String[] url = {Config.WEB_Services1,Config.UPDATE_LOCATION_DETAILS,"POST"};
//
//        if (Utilities.isNetworkAvailable(Dashboard.this)) {
//            apiManager.execute(url);
//        }else{
//
//        }
//    }

    void getStateCityList() {
        HashMap<String, Object> inputParams = new HashMap<String, Object>();


        SoapAPIManager apiManager = new SoapAPIManager(this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                            if (commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(Dashboard.this, commonDataInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(Dashboard.this, "Please check again!", false);
                            }
                            return;

                        }
                        JSONArray cityAry = commonDataInfo.getJSONArray("City");
                        JSONArray stateAry = commonDataInfo.getJSONArray("State");

                        GlobValues.setCityAry(cityAry);
                        GlobValues.setStateAry(stateAry);



                    }
                } catch (Exception e) {

                }
            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.GET_APP_COMMON_DATA, "POST"};

        if (Utilities.isNetworkAvailable(Dashboard.this)) {
            apiManager.execute(url);
        } else {

        }
    }
}

