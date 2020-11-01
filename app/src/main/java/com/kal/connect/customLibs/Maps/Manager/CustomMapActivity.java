package com.kal.connect.customLibs.Maps.Manager;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kal.connect.customLibs.Maps.coreLocation.CoreLocationManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.customLibs.mediaManager.MediaManager;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Utilities;
import com.kal.connect.utilities.UtilitiesInterfaces;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CustomMapActivity extends MediaManager {


    // MARK : Variables To Use
    private Place selectedPlace = null;
    com.google.android.libraries.places.api.model.Place selectedPlaceUpdate;
    private static final int PLACE_PICKER_REQUEST = 666;
    private int REQUEST_PLACE_PICKER = 1001;
    CoreLocationManager locManager = null;

    // MARK : Interfaces - To receive response as callback
    private PlacePickCallback placePickCallback;

    private PlacePickCallbackUpdate placePickCallbackUpdate;

    public interface PlacePickCallback {
        void receiveSelectedPlace(Boolean status, Place selectedPlace);

    }public interface PlacePickCallbackUpdate {
        void receiveSelectedPlace(Boolean status, com.google.android.libraries.places.api.model.Place selectedPlaceUpdate);
    }

    // MARK : Instance Methods

    // Launching place picker without Location - It will automatically picks the current device location
    public void showPlacePicker(final PlacePickCallback callback) {

        this.placePickCallback = callback;

        if (AppPreferences.getInstance().getSelectedLatitude().length() == 0) {
            Utilities.requestPermissions(CustomMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new UtilitiesInterfaces.PermissionCallback() {
                @Override
                public void receivePermissionStatus(Boolean isGranted) {
                    if (isGranted) {
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
        } else {
            Location pickerLocation = new Location("");
            pickerLocation.setLongitude(Double.parseDouble(AppPreferences.getInstance().getSelectedLongitude()));
            pickerLocation.setLatitude(Double.parseDouble(AppPreferences.getInstance().getSelectedLatitude()));
            showPlacePickerForLocation(pickerLocation, placePickCallback);
        }

    }

    // To Launch Place Picker with custom Location
    public void showPlacePickerForLocation(final Location userLocation, PlacePickCallback callback) {

        Utilities.requestPermissions(CustomMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new UtilitiesInterfaces.PermissionCallback() {
            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if (isGranted) {

                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    if (userLocation != null) {
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

        if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                String address = getAddress(place.getLatLng().latitude, place.getLatLng().longitude);

                System.out.println("Address: " + address);
                selectedPlace = PlacePicker.getPlace(this, data);
                if (selectedPlace != null && selectedPlace.getLatLng() != null) {

                    String placeName = "";
                    if (selectedPlace.getName() != null) {
                        placeName = selectedPlace.getName().toString();
                    }
                    if (selectedPlace.getAddress() != null) {
                        placeName = placeName + selectedPlace.getAddress().toString();
                    }
                    if (placeName != null) {
                        AppPreferences.getInstance().setLocationDetails("" + selectedPlace.getLatLng().latitude, "" + selectedPlace.getLatLng().longitude, placeName);
                    } else {
                        AppPreferences.getInstance().setLocationDetails("" + selectedPlace.getLatLng().latitude, "" + selectedPlace.getLatLng().longitude, address);
                    }

                }
                placePickCallback.receiveSelectedPlace(true, selectedPlace);
            } else {
                placePickCallback.receiveSelectedPlace(false, null);
            }

        }
        if ((requestCode == REQUEST_PLACE_PICKER) && (resultCode == RESULT_OK)) {
            com.google.android.libraries.places.api.model.Place place = PingPlacePicker.getPlace(data);

            String address = getAddress(place.getLatLng().latitude, place.getLatLng().longitude);

            System.out.println("Address: " + address);
            selectedPlaceUpdate = PingPlacePicker.getPlace(data);
            if (selectedPlaceUpdate != null && selectedPlaceUpdate.getLatLng() != null) {

                String placeName = "";
                if (selectedPlaceUpdate.getName() != null) {
                    placeName = selectedPlaceUpdate.getName().toString();
                }
                if (selectedPlaceUpdate.getAddress() != null) {
                    placeName = placeName + selectedPlaceUpdate.getAddress().toString();
                }
                if (placeName != null) {
                    AppPreferences.getInstance().setLocationDetails("" + selectedPlaceUpdate.getLatLng().latitude, "" + selectedPlaceUpdate.getLatLng().longitude, placeName);
                } else {
                    AppPreferences.getInstance().setLocationDetails("" + selectedPlaceUpdate.getLatLng().latitude, "" + selectedPlaceUpdate.getLatLng().longitude, address);
                }

            }
            placePickCallbackUpdate.receiveSelectedPlace(true, selectedPlaceUpdate);

            if (place != null) {
                Toast.makeText(this, "You selected the place: " + place.getName(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    String getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        String address = "";

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;

    }

    public void showPlacePickerUpgrade(final PlacePickCallbackUpdate callback) {


        this.placePickCallbackUpdate = callback;

        if (AppPreferences.getInstance().getSelectedLatitude().length() == 0) {
            Utilities.requestPermissions(CustomMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new UtilitiesInterfaces.PermissionCallback() {
                @Override
                public void receivePermissionStatus(Boolean isGranted) {
                    if (isGranted) {
                        locManager = new CoreLocationManager(CustomMapActivity.this, new CoreLocationManager.LocationCallback() {
                            @Override
                            public void receiveCurrentLocation(Location location) {
                                showPlacePickerForLocationUpdate(location, placePickCallbackUpdate);
                                locManager.stopLocationUpdates();
                            }

                            @Override
                            public void receiveCoreLocationStatus(Boolean status) {
                                showPlacePickerForLocationUpdate(null, placePickCallbackUpdate);
                            }
                        });
                    }
                }
            });
        } else {
            Location pickerLocation = new Location("");
            pickerLocation.setLongitude(Double.parseDouble(AppPreferences.getInstance().getSelectedLongitude()));
            pickerLocation.setLatitude(Double.parseDouble(AppPreferences.getInstance().getSelectedLatitude()));
            showPlacePickerForLocationUpdate(pickerLocation, placePickCallbackUpdate);
        }


    }

    private void showPlacePickerForLocationUpdate(Location pickerLocation, PlacePickCallbackUpdate placePickCallback) {

        Utilities.requestPermissions(CustomMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new UtilitiesInterfaces.PermissionCallback() {
            @Override
            public void receivePermissionStatus(Boolean isGranted) {

                if (isGranted) {
//                    AIzaSyAXXVTrvSJ8S-x9OfyjMFGJ_rlP_kHw2nA
                    PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
                    builder.setAndroidApiKey("AIzaSyAr2tbVoLqiZLD6cAC9M4wU0uL0aqMbGLs")
                            .setMapsApiKey("AIzaSyAr2tbVoLqiZLD6cAC9M4wU0uL0aqMbGLs");

//                    if (pickerLocation != null) {
//                        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(pickerLocation.getLatitude(), pickerLocation.getLongitude()), new LatLng(pickerLocation.getLatitude(), pickerLocation.getLongitude()));
//                        builder.setLatLngBounds(latLngBounds);
//                    }

                    try {
                        startActivityForResult(builder.build(CustomMapActivity.this), REQUEST_PLACE_PICKER);
                    } catch (Exception e) {
                        Log.e(this.getClass().getName(), e.getStackTrace().toString());
                    }

                }

            }
        });

    }


}
