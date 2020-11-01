package com.kal.connect.customLibs.appCustomization;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kal.connect.R;
import com.kal.connect.customLibs.noInternetAlert.ConnectivityReceiver;
import com.kal.connect.customLibs.noInternetAlert.NoInternetAlert;

import javax.net.ssl.SSLContext;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


/**
 * Created by lakeba_prabhu on 18/02/17.
 */

public class CustomApplication extends Application implements Application.ActivityLifecycleCallbacks, ConnectivityReceiver.ConnectivityReceiverListener{

    private FirebaseCrashlytics crashlytics;

    // For Handling the Notifications
    public static Context globalContext;
    public static Context getGlobalContext() {
        return globalContext;
    }

    // Internet Alert
    private NoInternetAlert noInternetAlert = null;

    // Get Current Activity's context globally
    public static void setGlobalContext(Context globalContext) {
        CustomApplication.globalContext = globalContext;
    }

    // MARK : Application Lifecycle
    @Override
    public void onCreate() {

        super.onCreate();
        globalContext = CustomApplication.this;
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()

        crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.log("Start logging!");

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()

                                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
//        );



        registerActivityLifecycleCallbacks(this);

        // Step 1: Internet Check -  Add Connectivity Listener
        // setConnectivityListener(CustomApplication.this);

        try {
            SSLContext.getInstance("TLSv1.2");
            ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    // MARK : To Track Internet connection

    // Step 2: Internet Check - Register receiver to get internet connection status
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    // Step 3: Internet Check -  Receive the network connection status
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        checkNetworkConnection();
    }

    // Step 4: Internet Check - Method to check and show internet connection alert
    private void checkNetworkConnection() {

        boolean isConnected = ConnectivityReceiver.isConnected();
        if(getGlobalContext() != null){

            if (isConnected && noInternetAlert != null) {
                noInternetAlert.hide();
            } else {
                noInternetAlert = new NoInternetAlert(getGlobalContext());
                noInternetAlert.show();
            }
        }

    }


    // MARK : Activity Lifecycle
    @Override
    public void onActivityResumed(Activity activity) {

        setGlobalContext(activity);

    }
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }
    @Override
    public void onActivityStarted(Activity activity) {

    }
    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }
    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    /* Steps to Use */
    /*

        Steps to user:
        --------------

        Step 1: Add the following lines in Manifest file

            <application android:name=".customLibs.customFont.CustomApplication"/>

        Step 2: To apply custom font extend perform change as follows

            class YourClass name extends Activity
                    to
            class YourClass name extends CustomActivity

        Step 3: Create Folder under "assets/fonts" folders under "src/main" directory and paste the required fonts for your project. Also change the default font inside this class.

        Step 4: Create Your own styles to use the fonts inside your project in any one of the following types


                    Type 1: for manual access

                        1. Add the following in side the styles.xml

                            <!-- Custom Font - To use manually Font Styles -->
                            <style name="CustomFont.Light" parent="android:TextAppearance">
                                <item name="fontPath">fonts/Quicksand-Light.ttf</item>
                            </style>
                            <style name="CustomFont.Regular" parent="android:TextAppearance">
                                <item name="fontPath">fonts/Quicksand-Regular.ttf</item>
                            </style>
                            <style name="CustomFont.Bold" parent="android:TextAppearance">
                                <item name="fontPath">fonts/Quicksand-Bold.ttf</item>
                            </style>
                            <style name="CustomFont.Title" parent="android:TextAppearance">
                                <item name="fontPath">fonts/PoiretOne-Regular.ttf</item>
                            </style>

                         2. Add the following property inside the xml widget for eg: textview, button etc.,
                            <TextView
                                        android:text="@string/hello_world"
                                        android:layout_width="wrap_content"
                                        android:layout_height="wrap_content"
                                        android:textAppearance="@style/CustomFont.Bold"/>


                    Type 2: access through widget - (Most preferred one!)

                        - This will automatically replaces the android native widget fonts to custom fonts

                        1. Add the following in styles.xml. The following will create a new style with "AppTheme.Widget.TextView" name
                            eg:

                            <!-- To Apply Custom Fonts by widget -->
                            <!--Custom Font - TextView-->
                            <style name="AppTheme.Widget.TextView" parent="android:Widget.Holo.Light.TextView">
                                <item name="fontPath">fonts/PoiretOne-Regular.ttf</item>
                            </style>

                        2. Add the created style to the android's styles
                            eg:

                             <!-- Base application theme. -->
                            <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
                                <!-- Customize your theme here. -->
                                <item name="colorPrimary">@color/colorPrimary</item>
                                <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
                                <item name="colorAccent">@color/colorAccent</item>
                                <item name="windowNoTitle">true</item>

                                <!-- Applying Custom Font - TextView Widget -->
                                <item name="android:textViewStyle">@style/AppTheme.Widget.TextView</item>

                            </style>

        Step 5: Add the following lines in Styles.xml


        Step 6: To Track internet connection

                        <!-- To track the internet connection -->
                        <uses-permission android:name="android.permission.INTERNET" />
                        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />


                        <!-- Broadcast receivers to Track internet connectivity -->
                        <receiver
                            android:name=".customLibs.noInternetAlert.ConnectivityReceiver"
                            android:enabled="true">
                            <intent-filter>
                                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
                            </intent-filter>
                        </receiver>

     */
}