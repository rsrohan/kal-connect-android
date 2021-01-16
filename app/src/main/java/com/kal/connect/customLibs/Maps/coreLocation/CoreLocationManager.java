package com.kal.connect.customLibs.Maps.coreLocation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kal.connect.customdialogbox.FlipProgressDialog;
import com.kal.connect.utilities.Utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by lakeba_prabhu on 14/04/17.
 * http://www.androhub.com/introduction-to-android-google-maps-v2/
 */

public class CoreLocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //List of Callbacks
    public interface AddressCallback {
        void receiveAddressFromLocation(HashMap<String, String> locationInfo);
    }

    //List of Callbacks
    public interface LocationCallback {
        void receiveCurrentLocation(Location location);
        void receiveCoreLocationStatus(Boolean status);
    }

    //To receive Address
    static AddressCallback addressCallback;
    static LocationCallback locationCallback;

    private Context mainContext;
    private static final String TAG = "CoreLocationManager";
    public static GoogleApiClient mGoogleApiClient;
    private Location currentLocation;
    private LocationRequest mLocationRequest;

    //to handle permissions
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    public FlipProgressDialog loading = null;
    public CoreLocationManager(Context context, LocationCallback callback) {
        loading = Utilities.showLoading(context);
        this.mainContext = context;
        initGoogleAPIClient();
        //checkPermissions();
        this.locationCallback = callback;
    }

    /* Step 1:  Initiate Google API Client  */
    private void initGoogleAPIClient() {

        Log.e(TAG, "Init google api client");
        mGoogleApiClient = new GoogleApiClient.Builder((Activity) mainContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    /*Step 2:  Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission((Activity) mainContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialog();
        } else
            showSettingDialog();

    }

    /*Step 3: Show Popup to access Location Permission  */
    private void requestLocationPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mainContext, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions((Activity) mainContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_INTENT_ID);
        } else {
            ActivityCompat.requestPermissions((Activity) mainContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }

    }

    /*Step 4: Show Location Access Dialog once access enabled start updating the current location */
    public void showSettingDialog() {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //Setting priotity of Location request to high
        mLocationRequest.setInterval(10 * 1000);//after 30sec the location will update
        mLocationRequest.setFastestInterval(10 * 1000);//5 sec Time interval

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off
        //start updating the location
        startLocationUpdates();

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location // requests here.
                        //getOriginLatLng();
                        break;
                    // Show alert to user to get location access
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user //a dialog.
                        try {
                            if(loading != null)
                                loading.dismiss();
                            status.startResolutionForResult((Activity) mainContext, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        if(loading != null)
                            loading.dismiss();
                        break;
                    case LocationSettingsStatusCodes.CANCELED:
                        locationCallback.receiveCoreLocationStatus(false);
                        break;
                }
            }
        });
    }


    /**
     * METHODS TO USE
     */

    /* Step 5:  Start Location Update */
    public void startLocationUpdates() {

        try {
            Log.e(TAG, "startLocationUpdates");
            if (ContextCompat.checkSelfPermission((Activity) mainContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        catch (Exception e){
            e.printStackTrace();
            // Utilities.showAlert(mainContext, mainContext.getResources().getString(R.string.error_location_check), true);
        }

    }

    //Stop Location Update Method
    public void stopLocationUpdates() {
        try{
            Log.e(TAG, "stopLocationUpdates");
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    /*****
     * CALL THIS IN ON STOP OF ACTIVITY LIFE CYCLE
     ****/
    protected void onStop() {
        //On Stop disconnect Api Client
        if (mGoogleApiClient != null) {
            Log.e(TAG, "onStop disconnect api client");
            mGoogleApiClient.disconnect();
        }
    }


    /*
        List of Listeners to handle
     */
    /**
     * Once GoogleApiClient object has been created get the initial location
     *
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {

        //On API Client Connection check if location permission is granted or not
        if (ContextCompat.checkSelfPermission((Activity) mainContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestLocationPermission();
        else {
            //If granted then get last know location and update the location label if last know location is not null
            Log.e(TAG, "onConnected get last know location");
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (currentLocation != null) {
                Log.e(TAG, "onConnected get last know location not null");
            } else
                Log.e(TAG, "onConnected get last know location null");

            if(loading != null)
                loading.dismiss();
        }

        //start updating the location
        showSettingDialog();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "Location Changed : " + location.getLatitude() + ", " + location.getLongitude());
        currentLocation = location;
        if(loading != null)
            loading.dismiss();
        locationCallback.receiveCurrentLocation(currentLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");
    }

    /* STEPS TO USE

        1. Paste the following method to handle the permissions
        2. CALL -> onStop() inside the stop method of fragment/activity
        3.
        coreLocationManager = new CoreLocationManager(LocActivity.this, new CoreLocationManager.LocationCallback() {
            @Override
            public void receiveCurrentLocation(Location location) {
                Toast.makeText(LocActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
            }
        });

     */
    /*** ADD THIS INSIDE THE ACTIVITY FILE **/
//    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
/* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case CoreLocationManager.ACCESS_FINE_LOCATION_INTENT_ID: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    //If permission granted show location dialog if APIClient is not null
//                    if(coreLocationManager.mGoogleApiClient == null){
//                        coreLocationManager.mGoogleApiClient.connect();
//                        coreLocationManager.showSettingDialog();
//                    }
//                    else
//                        coreLocationManager.showSettingDialog();
//
//                } else {
//                    Toast.makeText(LocActivity.this, "R.string.permission_denied", Toast.LENGTH_SHORT).show();
//                    // permission denied
//                }
//                return;
//            }
//        }
//    }
    /**
     * Get the location information by passing Latitude/Longitude
     *
     * @param latitude
     * @param longitude
     * @param context
     * @param callback
     */
    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, AddressCallback callback) {
        addressCallback = callback;
        final HashMap<String, String> addressInfo = new HashMap<String, String>();

        //Generate Address info in backgroundThread
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        try {

            //retrive the location information
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);

            if (addressList != null && addressList.size() > 0) {

                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
                result = sb.toString();

                //Build data in Hashmap to retrive
                //addressInfo.put("address", ((address.getAddressLine(0) != null) ? address.getAddressLine(0) : "") + " " + ((address.getLocality() != null) ? address.getLocality() : "") + " " + ((address.getPostalCode() != null) ? address.getPostalCode() : "") + " " + ((address.getCountryName() != null) ? address.getCountryName() : ""));
                addressInfo.put("address", ((address.getAddressLine(0) != null) ? address.getAddressLine(0) : ""));
                addressInfo.put("city", ((address.getLocality() != null) ? address.getLocality() : (address.getSubLocality() != null)? address.getSubLocality() : ""));
                addressInfo.put("state", ((address.getAdminArea() != null) ? address.getAdminArea() : (address.getSubLocality() != null)? address.getSubAdminArea() : ""));
                addressInfo.put("pincode", ((address.getPostalCode() != null) ? address.getPostalCode() : ""));
                addressInfo.put("country", ((address.getCountryName() != null) ? address.getCountryName() : ""));
                addressInfo.put("latitude", ((String.valueOf(latitude) != null) ? String.valueOf(address.getLatitude()) : "0"));
                addressInfo.put("longitude", ((String.valueOf(longitude) != null) ? String.valueOf(address.getLatitude()) : "0"));

            }

        } catch (IOException e) {
            Log.e(TAG, "Unable connect to Geocoder", e);
        } finally {
            //return the address info once it returs
            addressCallback.receiveAddressFromLocation(addressInfo);
        }
    }
}
