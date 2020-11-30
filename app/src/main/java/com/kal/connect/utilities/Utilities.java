package com.kal.connect.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kal.connect.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.kal.connect.customLibs.Callbacks.UpdateEMRCallBack;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.HTTP.Multipart.MultipartModel;
import com.kal.connect.customLibs.appCustomization.CustomApplication;
import com.kal.connect.customLibs.customAlert.CustomAlert;
import com.kal.connect.models.HospitalModel;
import com.kal.connect.adapters.LanguageListAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Utilities {

    private static Utilities instance = null;

    // To be used as a singleton
    public static Utilities getInstance() {
        if (instance == null)
            instance = new Utilities();
        return instance;
    }


    public static HospitalModel selectedHospitalModel;

    public static LanguageDialogClass languageDialogClass;
    /**
     * Convert JSONObject to HashMap
     */
    public static HashMap<String, Object> convertJSONObjectToHashMap(String inputJSON) {

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        HashMap<String, Object> haspMap = gson.fromJson(inputJSON, type);
        return haspMap;

    }

    public static HashMap<String, Object> convertJSONObjectToHashMapObject(String inputJSON) {

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        HashMap<String, Object> haspMap = gson.fromJson(inputJSON, type);
        return haspMap;

    }


    public static ArrayList<HashMap<String, Object>> convertJSONObjectToHashMapArray(JSONArray inputJSONArray) {


        ArrayList<HashMap<String, Object>> convertedObjects = new ArrayList<HashMap<String, Object>>();
        try {

            for (int loop = 0; loop < inputJSONArray.length(); loop++) {

                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Object>>() {
                }.getType();
                HashMap<String, Object> haspMap = gson.fromJson(inputJSONArray.get(loop).toString(), type);
                convertedObjects.add(haspMap);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertedObjects;

    }

    /**
     * Add Build Default params for Each API
     *
     * @return
     */
    public static HashMap<String, Object> getDefaultAPIParams() {

        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(AppPreferences.AUTH_TOKEN, AppPreferences.getInstance().getAuthToken());

        // Add Param if already logged in
        if (AppPreferences.getInstance().getUserInfo() != null && AppPreferences.getInstance().isLoggedIn()) {
            try {
                inputParams.put("user_id", AppPreferences.getInstance().getUserInfo().getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            inputParams.put("user_id", "");
        }
        return inputParams;

    }

    public static ArrayList<MultipartModel> getDefaultMultiPartAPIParams() {

        ArrayList<MultipartModel> inputParams = new ArrayList<MultipartModel>();
        inputParams.add(new MultipartModel(AppPreferences.AUTH_TOKEN, AppPreferences.getInstance().getAuthToken()));

        // Add Param if already logged in
        if (AppPreferences.getInstance().getUserInfo() != null && AppPreferences.getInstance().isLoggedIn()) {
            try {
                inputParams.add(new MultipartModel("user_id", AppPreferences.getInstance().getUserInfo().getString("id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return inputParams;

    }

    /**
     * Check for The API response data and return the result
     *
     * @param fromActivity
     * @param APIResponse
     * @return
     */
    public static Boolean isValidResponse(Context fromActivity, String APIResponse, String customMessage) {

        try {

            if (APIResponse != null) {

                JSONObject response = new JSONObject(APIResponse);
                if (response.getInt("status") == 1) {
                    return true;
                } else {
                    String message = (customMessage != null) ? customMessage : response.getString("msg");
                    Utilities.showAlert(fromActivity, message, false);
                }

            } else {
                Utilities.showAlert(fromActivity, fromActivity.getResources().getString(R.string.unknown_error), false);
            }

        } catch (JSONException e) {

            e.printStackTrace();
            Utilities.showAlert(fromActivity, fromActivity.getResources().getString(R.string.json_error), false);

        }
        return null;

    }

    /**
     * Validate API response without custom message
     *
     * @param fromActivity
     * @param APIResponse
     * @return
     */
    public static Boolean isValidResponse(Context fromActivity, String APIResponse) {
        return isValidResponse(fromActivity, APIResponse, null);
    }

    // Load Image via Library
    public static void loadImageWithPlaceHolerImage(ImageView inputImage, String imgURL, int placeHolder) {
        // http://square.github.io/picasso/

        // Load with default placeholder
        if(placeHolder == 0){
            Picasso.get().load(imgURL).placeholder(placeHolder).into(inputImage);
        }
        else {
            Picasso.get().load(imgURL).placeholder(CustomApplication.getGlobalContext().getResources().getDrawable(placeHolder)).into(inputImage);
        }


    }


    public static void loadImageWithPlaceHoler(ImageView inputImgView, String imgURL, String nameToSet) {

        TextDrawable firstLetterPlaceHolder = null;
        if(nameToSet != null && nameToSet.length() > 0) {
            int primaryColor = Color.parseColor("#F99E2D");
            String firstLetter = "" + nameToSet.trim().toUpperCase().charAt(0);
            firstLetterPlaceHolder = TextDrawable.builder().buildRect(firstLetter, primaryColor);
            inputImgView.setImageDrawable(firstLetterPlaceHolder);
        }

        if (imgURL != null && imgURL.length() > 0) {
            Picasso.get().load(imgURL).placeholder(firstLetterPlaceHolder).into(inputImgView);
        }

    }

    public static void loadImageWithPlaceHoler(Context context,CircularRevealCardView inputImgView, String imgURL, String nameToSet) {

        TextDrawable firstLetterPlaceHolder = null;
        if(nameToSet != null && nameToSet.length() > 0) {
            int primaryColor = Color.parseColor("#F99E2D");
            String firstLetter = "" + nameToSet.trim().toUpperCase().charAt(0);
            firstLetterPlaceHolder = TextDrawable.builder().buildRect(firstLetter, primaryColor);
//            inputImgView.setImageDrawable(firstLetterPlaceHolder);
            inputImgView.setBackground(firstLetterPlaceHolder);
        }

        if (imgURL != null && imgURL.length() > 0) {
//            Picasso.get().load(imgURL).placeholder(firstLetterPlaceHolder).into(inputImgView);
            Picasso.get().load(imgURL)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                            /* Save the bitmap or do something with it here */

                            //Set it in the ImageView
                            inputImgView.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }

    }


    /**
     * Hides the keyboard
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e("Error : Hide - Keyboard", e.toString(), e);
        }
    }

    /**
     * Internet check
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(final Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressLint("MissingPermission") NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int length = 0; length < info.length; length++) {
                    if (info[length].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }


    /**
     * Show Toast even from background thread, message should be int for internationalization
     *
     * @param context
     * @param message
     */
    public static void showToast(final Context context, final String message) {
        if (context != null) {
            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * @param context
     * @param message
     */
    public static void showAlert(final Context context, final String message, final Boolean status) {
        if (context != null) {
            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    CustomAlert.showAlert(context, message, status);
                }
            });
        }
    }

    /**
     * Validate Email
     *
     * @param target
     * @return
     */
    public static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Set error in user's custom style
     *
     * @param inputField
     * @param errorMsg
     */
    public static void setError(Context context, EditText inputField, final String errorMsg) {

        CustomAlert.showAlert(context, errorMsg, false);
        inputField.requestFocus();

    }

    public static String getStringById(Context context, int stringWithID) {
        return context.getString(stringWithID);
    }

    /**
     * Perform Validation
     *
     * @param inputField
     * @param fieldToDisplay
     * @param isEmail
     * @param minLength
     * @param maxLength
     * @return
     */
    public static boolean validate(Context context, EditText inputField, String fieldToDisplay, Boolean isEmail, int minLength, int maxLength) {

        String inputString = inputField.getText().toString();
        String errorMsg = "";
        if (inputString.trim().length() == 0) {
            errorMsg = context.getString(R.string.error_should_not_empty, fieldToDisplay);
            setError(context, inputField, errorMsg);
            return false;
        } else if (inputString.length() < minLength) {
            errorMsg = context.getString(R.string.error_min_length, fieldToDisplay, minLength);
            setError(context, inputField, errorMsg);
            return false;
        } else if (inputString.length() > maxLength) {
            errorMsg = context.getString(R.string.error_max_length, fieldToDisplay, maxLength);
            setError(context, inputField, errorMsg);
            return false;
        } else if (isEmail && !isValidEmail(inputString)) {
            errorMsg = context.getString(R.string.error_invalid_email);
            setError(context, inputField, errorMsg);
            return false;
        }
        return true;
    }


    /**
     * UI Releavnt Methods
     */

    /**
     * @param view
     * @param size
     */
    //http://stackoverflow.com/questions/28578701/create-android-shape-background-programmatically
    public static void setRoundedCorner(View view, float size, String color) {

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(size);
        shape.setColor(Color.parseColor(color));
        // shape.setStroke(3, borderColor);
        view.setBackground(shape);

    }

    /**
     * Set Rounc Corner for views
     *
     * @param views
     * @param cornerRadius
     * @param borderColor  To use : Utilities.setRoundedCornerForViews(new View[]{textView}, 10, Config.DefaultSelectionColor);
     */
    public static void setRoundedCornerForViews(View views[], float cornerRadius, String borderColor) {
        for (int loop = 0; loop < views.length; loop++) {

            // Initialize a new GradientDrawable
            GradientDrawable gd = new GradientDrawable();

            // Specify the shape of drawable
            gd.setShape(GradientDrawable.RECTANGLE);

            // Set the fill color of drawable
            if (views[loop].getBackground() != null) {
                gd.setColor(((ColorDrawable) views[loop].getBackground()).getColor());
            } else {
                gd.setColor(Color.TRANSPARENT); // make the background transparent
            }

            // Create a 2 pixels width red colored border for drawable
            gd.setStroke(2, Color.parseColor(borderColor)); // border width and color

            // Make the border rounded
            gd.setCornerRadius(cornerRadius); // border corner radius

            // Finally, apply the GradientDrawable as TextView background
            views[loop].setBackground(gd);

        }
    }

    /**
     * Builds circular view
     *
     * @param view
     */
    public static void makeCircularView(View view) {
        float roundedSize = (float) (view.getLayoutParams().height / 2);
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(roundedSize);
        view.setBackground(shape);
    }

    /**
     * Creates a Border on a view
     *
     * @param view
     * @param borderColor
     * @param size
     */
    public static void createBorder(View view, String borderColor, int size) {
        GradientDrawable shape = new GradientDrawable();
        shape.setStroke(size, Color.parseColor(borderColor));
        view.setBackground(shape);
    }

    /**
     * Push Animation
     *
     * @param activity
     */
    public static void pushAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
    }

    /**
     * Pop Animation
     *
     * @param activity
     */
    public static void popAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
    }

    /**
     * Show Loading
     *
     * @param context
     * @return
     */
    public static ProgressDialog showLoading(Context context) {

        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;

    }

    /**
     * Hide Loading
     *
     * @param pDialog
     */
    public static void hideLoading(ProgressDialog pDialog) {

        if (pDialog != null)
            pDialog.dismiss();

    }


    /**
     * Set PullDown to refresh properties
     *
     * @param context
     * @param pullToRefresh
     */
//    public static void setPullDownProperties(Context context, MaterialRefreshLayout pullToRefresh, Boolean enableLoadMore) {
//
//        pullToRefresh.setProgressColors(new int[]{context.getResources().getColor(R.color.material_blue), context.getResources().getColor(R.color.white)});
//        pullToRefresh.setWaveColor(context.getResources().getColor(R.color.colorAccent));
//        pullToRefresh.setIsOverLay(false);
//        pullToRefresh.setWaveShow(true);
//        pullToRefresh.setLoadMore(enableLoadMore);
//        pullToRefresh.setShowProgressBg(true);
//
//    }

    /**
     * Check for the Enable permissions
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;

    }

    /**
     * Date String with format
     *
     * @return
     */
    public static String getCurrentDate(String format) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;

    }

    public static String changeStringFormat(String dateString, String fromFormat, String toFormat){
        DateFormat fromDateFormat = new SimpleDateFormat(fromFormat);
        DateFormat toDateFormat = new SimpleDateFormat(toFormat);
        try{
            Date inputDate = fromDateFormat.parse(dateString);
            String outputDateString = toDateFormat.format(inputDate);
            return outputDateString;
        }catch (Exception e){}

        return "";
    }

    /**
     * Time String with format
     *
     * @return
     */
    public static String getCurrentTime(String format) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;

    }

    /**
     * Date String
     *
     * @return
     */
    public static String getCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;

    }

    /**
     * Time String
     *
     * @return
     */
    public static String getCurrentTime() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;

    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static void updateLanguageSettings(Context context) {

        if (AppPreferences.localaizationStatus) {

            // if (AppPreferences.getInstance(context).retrieveLanguage() != null) {
            if (AppPreferences.getInstance().retrieveLanguage() != null) {
                AppPreferences.localaizationStatus = false;
                Locale locale = new Locale(AppPreferences.getInstance().retrieveLanguage());
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }

        }

    }

    /**
     * To Set any view color programmatically
     *
     * @param view
     * @param colorToSet
     */
    public static void setColorForView(View view, String colorToSet) {
        view.getBackground().setColorFilter(Color.parseColor(colorToSet), PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Create Alertview dynamically maxium of buttons -> 3 minimum buttons -> 1
     *
     * @param fromActivity
     * @param message
     * @param options       -> new String[]{"Cancel", "Yes", "No" }
     * @param alertCallback
     */
    public static void showAlertDialogWithOptions(Activity fromActivity, String message, String[] options, UtilitiesInterfaces.AlertCallback alertCallback) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(fromActivity);
        alertDialog.setTitle(Config.AppName);
        alertDialog.setMessage(message);
        final UtilitiesInterfaces.AlertCallback callback = alertCallback;
        for (int count = 0; count < options.length; count++) {

            final int buttonIndex = count;
            if (options.length == 3 && count == 0) {
                alertDialog.setNeutralButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });

            } else if ((options.length == 2 && count == 0) || (options.length == 3 && count == 1)) {

                alertDialog.setNegativeButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });

            } else if (options.length == 1 || count == 2 || count == 1) {
                alertDialog.setPositiveButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });
            }

        }
        alertDialog.setCancelable(false);

        alertDialog.show();

    }


    /**
     * Create Alertview dynamically maxium of buttons -> 3 minimum buttons -> 1
     *
     * @param fromActivity
     * @param message
     * @param options       -> new String[]{"Cancel", "Yes", "No" }
     * @param alertCallback
     */
    public static void showAlertDialogWithOptions(Activity fromActivity, boolean cancelable, String message, String[] options, UtilitiesInterfaces.AlertCallback alertCallback) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(fromActivity);
        alertDialog.setTitle(Config.AppName);
        alertDialog.setMessage(message);
        final UtilitiesInterfaces.AlertCallback callback = alertCallback;
        for (int count = 0; count < options.length; count++) {

            final int buttonIndex = count;
            if (options.length == 3 && count == 0) {
                alertDialog.setNeutralButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });

            } else if ((options.length == 2 && count == 0) || (options.length == 3 && count == 1)) {

                alertDialog.setNegativeButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });

            } else if (options.length == 1 || count == 2 || count == 1) {
                alertDialog.setPositiveButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });
            }

        }
        alertDialog.setCancelable(cancelable);

        alertDialog.show();

    }

    /**
     * Create Alertview dynamically maxium of buttons -> 3 minimum buttons -> 1
     *
     * @param fromActivity
     * @param hint
     * @param message
     * @param options       -> new String[]{"Cancel", "Yes", "No" }
     * @param alertCallback
     */
    public static void showAlertDialogWithEditText(Activity fromActivity, String hint,String message, String[] options, UtilitiesInterfaces.AlertCallback alertCallback) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(fromActivity);

        final EditText edittext = new EditText(fromActivity);
        edittext.setHint(hint);
        edittext.setText(message);
        alertDialog.setView(edittext);

        alertDialog.setTitle(Config.AppName);
//        alertDialog.setMessage(message);
        final UtilitiesInterfaces.AlertCallback callback = alertCallback;
        for (int count = 0; count < options.length; count++) {

            final int buttonIndex = count;
            if (options.length == 3 && count == 0) {
                alertDialog.setNeutralButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });

            } else if ((options.length == 2 && count == 0) || (options.length == 3 && count == 1)) {

                alertDialog.setNegativeButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });

            } else if (options.length == 1 || count == 2 || count == 1) {
                alertDialog.setPositiveButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });
            }

        }
        alertDialog.show();

    }

    /**
     * Request Permission to access private APIs in Android
     *
     * @param fromActivity
     * @param permissions
     */
    public static void requestPermissions(final Activity fromActivity, String[] permissions, UtilitiesInterfaces.PermissionCallback permissionCallback) {

        // To Receive the permission status as callbacks
        final UtilitiesInterfaces.PermissionCallback callback = permissionCallback;

        // Check Private Permissions if >= Mashmallow
        if (Build.VERSION.SDK_INT >= 23) {

            Dexter.withActivity(fromActivity).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {


                    if (report.areAllPermissionsGranted()) {
                        // Perform action
                        callback.receivePermissionStatus(true);
                    } else {

                        // Ask user to enable permission from App's Settings page
                        Utilities.showAlertDialogWithOptions(fromActivity, "This app needs permission to use this feature. You can grant them in app settings.", new String[]{"Cancel", "OK"}, new UtilitiesInterfaces.AlertCallback() {
                            @Override
                            public void onOptionClick(DialogInterface dialog, int buttonIndex) {

                                if (buttonIndex == 1) {

                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", fromActivity.getPackageName(), null);
                                    intent.setData(uri);
                                    fromActivity.startActivityForResult(intent, 101);
                                    // return the callback to perform the action again
                                    callback.receivePermissionStatus(false);

                                }

                            }
                        });

                    }

                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    // Ask user to enable
                    token.continuePermissionRequest();
                }

            }).withErrorListener(new PermissionRequestErrorListener() {
                @Override
                public void onError(DexterError error) {
                    Log.e("For Privacy Permission", "There was an error: " + error.toString());
                }
            }).check();

        }
        // If Below Mashmallow return true - since it's not restricted to acess
        else {
            callback.receivePermissionStatus(true);
        }

    }



    public static String dateRT(String date){
        return date.substring(0, date.indexOf("T")) + " ";
    }

    public void updateEMR(Context context, HashMap<String, Object> inputParams, final UpdateEMRCallBack updateEMRCallback){

        HashMap<String, Object> selectedAppointmentData  =  GlobValues.getSelectedAppointmentData();
        inputParams.put("appointmentID", selectedAppointmentData.get("appointmentId").toString());
        inputParams.put("ComplaintID", selectedAppointmentData.get("ComplaintID").toString());

        inputParams.put("ModifiedDate", Utilities.getCurrentDate("MM/dd/yyyy"));

        try{
            JSONObject userInfo = AppPreferences.getInstance().getUserInfo();
            inputParams.put("patientname",userInfo.getString("FirstName")+" "+userInfo.getString("LastName"));
            inputParams.put("firstname",userInfo.getString("FirstName"));
            inputParams.put("lastname",userInfo.getString("LastName"));
        }catch (Exception e){

        }


        SoapAPIManager apiManager = new SoapAPIManager(context, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    JSONArray responseAry = new JSONArray(response);
                    if(responseAry.length()>0){
                        JSONObject userInfo = responseAry.getJSONObject(0);
                        String message = (userInfo.has("Message") && !userInfo.getString("Message").isEmpty() )?userInfo.getString("Message"):"";
                        if(userInfo.has("APIStatus") && Integer.parseInt(userInfo.getString("APIStatus")) == -1){
                            message = message.isEmpty()?"Please try again":message;
                            updateEMRCallback.updateEMRCallback(false);
                        }else{
                            message = message.isEmpty()?"Data updated successfully":message;
                            try {
                                updateEMRCallback.updateEMRCallback(true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Utilities.showAlert(context,message,false);
                    }

                }catch (Exception e){
                    updateEMRCallback.updateEMRCallback(false);

                }


            }
        },true);
        String[] url = {Config.WEB_Services1,Config.UPDATE_EMR,"POST"};

        if (Utilities.isNetworkAvailable(context)) {
            apiManager.execute(url);
        }else{

        }

    }

    public void updateEMR(Context context,HashMap<String, Object> inputParams){


        HashMap<String, Object> selectedAppointmentData  =  GlobValues.getSelectedAppointmentData();
        inputParams.put("appointmentID", selectedAppointmentData.get("appointmentId").toString());
        inputParams.put("ComplaintID", selectedAppointmentData.get("ComplaintID").toString());

        inputParams.put("ModifiedDate", Utilities.getCurrentDate("MM/dd/yyyy"));

        try{
            JSONObject userInfo = AppPreferences.getInstance().getUserInfo();
            inputParams.put("patientname",userInfo.getString("FirstName")+" "+userInfo.getString("LastName"));
            inputParams.put("firstname",userInfo.getString("FirstName"));
            inputParams.put("lastname",userInfo.getString("LastName"));
        }catch (Exception e){

        }


        SoapAPIManager apiManager = new SoapAPIManager(context, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    JSONArray responseAry = new JSONArray(response);
                    if(responseAry.length()>0){
                        JSONObject userInfo = responseAry.getJSONObject(0);
                        String message = (userInfo.has("Message") && !userInfo.getString("Message").isEmpty() )?userInfo.getString("Message"):"";
                        if(userInfo.has("APIStatus") && Integer.parseInt(userInfo.getString("APIStatus")) == -1){
                            message = message.isEmpty()?"Please try again":message;
                        }else{
                            message = message.isEmpty()?"Data updated successfully":message;
                        }

                        Utilities.showAlert(context,message,false);
                    }

                }catch (Exception e){

                }


            }
        },true);
        String[] url = {Config.WEB_Services1,Config.UPDATE_EMR,"POST"};

        if (Utilities.isNetworkAvailable(context)) {
            apiManager.execute(url);
        }else{

        }
    }

    public static String getStatus(Context context, String status){
        String localisedStatus = "";
        switch (status.toLowerCase()){
            case "active":
                localisedStatus = context.getResources().getString(R.string.active);
                break;
            case "consulted":
                localisedStatus = context.getResources().getString(R.string.consulted);
                break;
            case "not consulted":
                localisedStatus = context.getResources().getString(R.string.not_consulted);
                break;
            default:
                localisedStatus = status;
                break;
        }
        return localisedStatus;
    }


    public static void updateLocation(Context context,HashMap<String, Object> inputParams){

        SoapAPIManager apiManager = new SoapAPIManager(context, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    JSONArray responseAry = new JSONArray(response);
                    if(responseAry.length()>0){
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if(commonDataInfo.getString("APIStatus").equals("1")){
                            JSONObject userInfo = AppPreferences.getInstance().getUserInfo();
                            userInfo.put("Lattitude",inputParams.get("Lattitude").toString());
                            userInfo.put("Longitude",inputParams.get("Longitude").toString());
                            userInfo.put("LocationAddress",inputParams.get("LocationAddress").toString());
                            Utilities.showAlert(context, "Location updated successfully", true);
                            AppPreferences.getInstance().setLoginInfo(userInfo.toString());
                        }
                    }
                }catch (Exception e){

                }
            }
        },true);
        String[] url = {Config.WEB_Services1,Config.UPDATE_LOCATION_DETAILS,"POST"};

        if (Utilities.isNetworkAvailable(context)) {
            apiManager.execute(url);
        }else{

        }
    }

    public static void openLanguageSelection(Activity a){
        languageDialogClass = new LanguageDialogClass(a);
        languageDialogClass.show();
    }

    public static void setAppLocale(Context context){
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(AppPreferences.getInstance().retrieveLanguage().toLowerCase()));
        } else {
            config.locale = new Locale(AppPreferences.getInstance().retrieveLanguage().toLowerCase());
        }
//        getBaseContext().getResources().updateConfiguration(config,
//                context.getBaseContext().getResources().getDisplayMetrics());
        resources.updateConfiguration(config, dm);


    }

    public static class LanguageDialogClass extends Dialog {

        public Activity activity;
        public Dialog dialog;
        public Button yes, no;

        public EditText password, confirmPassword;

        @BindView(R.id.language_list)
        RecyclerView languageList;



        LanguageListAdapter dataAdapter;

        public LanguageDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.activity = a;
        }

        ArrayList<HashMap<String, Object>> dataItems = new ArrayList<HashMap<String, Object>>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.language_list);

            ButterKnife.bind(this);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);

            languageList.setNestedScrollingEnabled(false);
            languageList.setLayoutManager(mLayoutManager);
            languageList.setItemAnimator(new DefaultItemAnimator());
//            languageList.setLayoutManager(new GridLayoutManager(activity, 2));

            HashMap<String, Object> item1 = new HashMap<String, Object>();
            item1.put("languageName","English");
            item1.put("localeName","English");
            item1.put("languageCode","en");


            HashMap<String, Object> item2 = new HashMap<String, Object>();

            item2.put("languageName","Gujarati");
            item2.put("localeName","ગુજરાતી ");
            item2.put("languageCode","gu");

            HashMap<String, Object> item3 = new HashMap<String, Object>();

            item3.put("languageName","Hindi");
            item3.put("localeName","हिन्दी");
            item3.put("languageCode","hi");


            dataItems.add(item1);
            dataItems.add(item2);
            dataItems.add(item3);


            dataAdapter = new LanguageListAdapter(dataItems, this.activity);

            dataAdapter.setOnItemClickListener(new LanguageListAdapter.ItemClickListener() {

                @Override
                public void onItemClick(int position, View v) {
                    HashMap<String, Object> item = dataItems.get(position);


                    dataAdapter.notifyDataSetChanged();
                    languageDialogClass.dismiss();

                    LocalizeManager.setNewLocale(activity,item.get("languageCode").toString());
                    Intent i = new Intent(activity.getApplicationContext(), Splash.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getContext().startActivity(i);
                }

            });

            languageList.setAdapter(dataAdapter);

        }

    }

}
