package com.kal.connect.customLibs.Maps.customMap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by lakeba_prabhu on 02/04/17.
 */

public class MarkerModel {

    String title;
    String description;
    LatLng latLng;
    BitmapDescriptor icon;
    Marker markerInMap;

    // U can set and retrive Model classes for each marker if u want
    // eg : EmployeeData employeeData;

    public MarkerModel(LatLng latLng, String title, String description, int markerIcon) {

        this.latLng = latLng;
        this.title = title;
        this.description = description;
        this.icon = BitmapDescriptorFactory.fromResource(markerIcon);

    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Marker getMarkerInMap() {
        return markerInMap;
    }

    public void setMarkerInMap(Marker markerInMap) {
        this.markerInMap = markerInMap;
    }
}
