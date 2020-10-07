package com.kal.connect.customLibs.Maps.Manager;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kal.connect.customLibs.Maps.coreLocation.CoreLocationManager;
import com.kal.connect.customLibs.mediaManager.MediaManager;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Utilities;
import com.kal.connect.utilities.UtilitiesInterfaces;

public class CustomMapActivity extends MediaManager{


    // MARK : Variables To Use
    private Place selectedPlace = null;
    private static final int PLACE_PICKER_REQUEST = 666;
    CoreLocationManager locManager = null;

    // MARK : Interfaces - To receive response as callback
    private PlacePickCallback placePickCallback;
    public interface PlacePickCallback {
        void receiveSelectedPlace(Boolean status, Place selectedPlace);
    }

    // MARK : Instance Methods

    // Launching place picker without Location - It will automatically picks the current device location
    public void showPlacePicker(final PlacePickCallback callback) {

        this.placePickCallback = callback;

        if(AppPreferences.getInstance().getSelectedLatitude().length() == 0){
            Utilities.requestPermissions(CustomMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new UtilitiesInterfaces.PermissionCallback(){
                @Override
                public void receivePermissionStatus(Boolean isGranted) {
                    if(isGranted) {
                        locManager = new CoreLocationManager(CustomMapActivity.this, new CoreLocationManager.LocationCallback() {
                            @Override
                            public void receiveCurrentLocation(Location location) {
                                showPlacePickerForLocation(location, placePickCallback);
                                locManager.stopLocationUpdates();
                            }

                            @Override
                            public void receiveCoreLocationStatus(Boolean status) {
                                showPlacePickerForLocation(null, placePickCallback);
                            }
                        });
                    }
                }
            });
        }
        else {
            Location pickerLocation = new Location("");
            pickerLocation.setLongitude(Double.parseDouble(AppPreferences.getInstance().getSelectedLongitude()));
            pickerLocation.setLatitude(Double.parseDouble(AppPreferences.getInstance().getSelectedLatitude()));
            showPlacePickerForLocation(pickerLocation, placePickCallback);
        }

    }

    // To Launch Place Picker with custom Location
    public void showPlacePickerForLocation(final Location userLocation, PlacePickCallback callback) {

        Utilities.requestPermissions(CustomMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new UtilitiesInterfaces.PermissionCallback(){
            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if(isGranted) {

                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    if(userLocation != null){
                        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), new LatLng(userLocation.getLatitude(), userLocation.getLongitude()));
                        builder.setLatLngBounds(latLngBounds);
                    }

                    try {
                        startActivityForResult(builder.build(CustomMapActivity.this), PLACE_PICKER_REQUEST);
                    } catch (Exception e) {
                        Log.e(this.getClass().getName(), e.getStackTrace().toString());
                    }

                }

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PLACE_PICKER_REQUEST){

            if (resultCode == RESULT_OK) {
                selectedPlace = PlacePicker.getPlace(this, data);
                if(selectedPlace != null && selectedPlace.getLatLng() != null){

                    String placeName = "";
                    if(selectedPlace.getName() != null){
                        placeName = selectedPlace.getName().toString();
                    }
                    if(selectedPlace.getAddress() != null){
                        placeName = placeName + selectedPlace.getAddress().toString();
                    }
                    AppPreferences.getInstance().setLocationDetails("" + selectedPlace.getLatLng().latitude, "" + selectedPlace.getLatLng().longitude, placeName);

                }
                placePickCallback.receiveSelectedPlace(true, selectedPlace);
            }
            else {
                placePickCallback.receiveSelectedPlace(false, null);
            }

        }
        // To handling the image/gallery pick feature
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

}
