package com.kal.connect.customLibs.Maps.customMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.APIManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lakeba_prabhu on 14/04/17.
 * http://www.androhub.com/introduction-to-android-google-maps-v2/
 */

public class CustomMap extends MapView implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    /**
     * To Receive the google Map events
     */
    public interface CustomMapCallback {

        void onCustomMapReady();

        void onCustomMapMakerClicked(Marker marker);

    }

    public static GoogleMap mapView;
    private Context mContext;
    private CustomMapCallback mapCallback;
    private float zoomSize = 10.0f;

    /* Step 1: Set Up google map if Google Play Services Exists */
    public CustomMap(Context context, SupportMapFragment mFragment, CustomMapCallback callback) {

        super(context);
        this.mapCallback = callback;
        this.mContext = context;

        if (isGooglePlayServicesAvailable((Activity) context))
            mFragment.getMapAsync(this);

    }

    /*Step 2: Once Gooogle map ready init the required methods */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapView = googleMap;
        mapView.setOnMarkerClickListener(this);
        mapCallback.onCustomMapReady();
        enableUISettings();

    }

    //Step 4: Enable required UI
    public void enableUISettings() {

        mapView.getUiSettings().setMapToolbarEnabled(false);

        // Additional Map Properties
        //mapView.getUiSettings().setCompassEnabled(true);
        //mapView.getUiSettings().setRotateGesturesEnabled(true);
        //mapView.getUiSettings().setZoomControlsEnabled(false);
        //mapView.getUiSettings().setZoomGesturesEnabled(false);
        //mapView.getUiSettings().setTiltGesturesEnabled(false);
        //mapView.getUiSettings().setScrollGesturesEnabled(false);

    }

    /* Step 3: MARKER CLICK EVENTS */
    @Override
    public boolean onMarkerClick(Marker marker) {

        mapCallback.onCustomMapMakerClicked(marker);
        return false;

    }

    //To Animate to Given LatLng
    public void moveCameraTo(LatLng latLng) {

        mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomSize), 1500, null);

    }

    //To Add Marker to Google Map by using MarkerModel object
    public void addMarker(MarkerModel markerInfo) {

        Marker marker = mapView.addMarker(new MarkerOptions().position(markerInfo.latLng).title(markerInfo.title).snippet(markerInfo.description));
        marker.setIcon(markerInfo.icon);
        marker.setTag(markerInfo);
        moveCameraTo(markerInfo.latLng);

    }

    public void clearAll() {

        mapView.animateCamera(CameraUpdateFactory.zoomOut(), 1500, null);
        mapView.clear();

    }

    //To Add List of Markers to Google Map
//    public void addMarkers(ArrayList<MarkerModel> markersList) {
//
//        // For covering all markers on map
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//        MarkerModel lastMarker = null;
//        for (MarkerModel markerInfo : markersList) {
//
//            Marker marker = mapView.addMarker(new MarkerOptions().position(markerInfo.latLng).title(markerInfo.title).snippet(markerInfo.description));
//            marker.setIcon(markerInfo.icon);
//            marker.setTag(markerInfo);
//            lastMarker = markerInfo;
//
//            // For covering all markers on map
//            builder.include(marker.getPosition());
//
//        }
//
//        if (lastMarker != null){
//
//            moveCameraTo(lastMarker.latLng);
//            int padding = 10; // offset from edges of the map in pixels
//            LatLngBounds bounds = builder.build();
//            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//            // mapView.animateCamera(cu);
//            mapView.animateCamera(cu, 3000, null);
//
//        }
//
//
//    }

    //To Add List of Markers to Google Map
    public void addMarkers(ArrayList<MarkerModel> markersList) {

        // For covering all markers on map
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        MarkerModel lastMarker = null;
        for (MarkerModel markerInfo : markersList) {

            Marker marker = mapView.addMarker(new MarkerOptions().position(markerInfo.latLng).title(markerInfo.title).snippet(markerInfo.description));
            marker.setIcon(markerInfo.icon);
            marker.setTag(markerInfo);
            lastMarker = markerInfo;

            // Set the marker to access it any whare after added
            markerInfo.markerInMap = marker;

            // For covering all markers on map
            builder.include(marker.getPosition());

        }

        if (lastMarker != null){

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.75);
            int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mapView.animateCamera(cu, 3000, null);

        }


    }

    //To Check Google Play services available or not
    private boolean isGooglePlayServicesAvailable(Activity activity) {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);

        if (status != ConnectionResult.SUCCESS) {

            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;

        }
        return true;

    }


    /**
     * Place services
     */
    public static void drawDirectionsBetweenTwoPoints(Context mContext, Location fromLocation, Location toLocation) {

        if (mapView != null) {

            String directionURL = getDirectionsUrl(fromLocation, toLocation);

            APIManager apiManager = new APIManager(mContext, null, new APICallback() {

                @Override
                public void responseCallback(Context context, String response) throws JSONException {

                    if (response != null) {

                        RouteParserTask parserTask = new RouteParserTask();
                        // Invokes the thread for parsing the JSON data
                        parserTask.execute(response);

                    }

                }
            }, null,false);
            String[] url = {directionURL, "POST"};
            apiManager.execute(url);

        }

    }

    private static String getDirectionsUrl(Location fromLocation, Location toLocation) {

        // Origin of route
        String str_origin = "origin=" + fromLocation.getLatitude() + "," + fromLocation.getLongitude();
        // Destination of route
        String str_dest = "destination=" + toLocation.getLatitude() + "," + toLocation.getLongitude();

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;

    }

    private static class RouteParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            if(result != null && result.size() > 0){

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);

                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                }

                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null) {
                    mapView.addPolyline(lineOptions);
                } else {
                    Log.d("onPostExecute", "without Polylines drawn");
                }

            }
        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {

        if(marker != null && mapView != null){

// Enable this To test the marker movement
//            Random r = new Random();
//            int randomHeading = r.nextInt(360);
//            final LatLng toPosition = SphericalUtil.computeOffset(newLatLong, 5, randomHeading);

            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            Projection proj = mapView.getProjection();
            Point startPoint = proj.toScreenLocation(marker.getPosition());
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final long duration = 500;
            final Interpolator interpolator = new LinearInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t * toPosition.longitude + (1 - t)
                            * startLatLng.longitude;
                    double lat = t * toPosition.latitude + (1 - t)
                            * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        if (hideMarker) {
                            marker.setVisible(false);
                        } else {
                            marker.setVisible(true);
                        }
                    }
                }
            });

        }

    }

}
