package com.kal.connect.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kal.connect.customLibs.JSONHandler.JSONHandler;
import com.kal.connect.customLibs.appCustomization.CustomApplication;
import com.kal.connect.modules.authentication.SignIn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppPreferences {

    //Configurations
    static int PRIVATE_MODE = 0;
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    static Context context;

    // Apps - Default Keys
    static final String PREF_NAME = Config.AppName + "_PREFERENCES";
    static final String IS_LOGGEDIN = "IsLoggedIn";
    public static final String AUTH_TOKEN = "api_token";
    public static final String LANGUAGE = "Language";

    // Project Specific
    public static final String USER_INFO = "USER_INFO";
    public static final String SITE_URL = "site_url";
    public static Boolean localaizationStatus = false;


    // Application's Preferences
    public static final String SELECTED_CATEGORIES = "SAVED_CATEGORIES";
    public static final String SAVED_CATEGORIES = "SELECTED_CATEGORIES";

    public static final String SELECTED_LATTITUDE = "SELECTED_LATTITUDE";
    public static final String SELECTED_LONGITUDE = "SELECTED_LONGITUDE";
    public static final String SELECTED_ADDRESS = "SELECTED_ADDRESS";

    public static final String DEVICE_TOKEN = "DEVICE_TOKEN";

    public static final String CITY_DATA = "CITY_DATA";
    public static final String STATE_DATA = "STATE_DATA";
    public static final String APP_KILLED = "APP_KILLED";

    public static final String LOCATION_REQUESTED = "LOCATION_REQUESTED";


    /**
     * Singleton Instance to read
     */
    static AppPreferences instance = null;
    public static AppPreferences getInstance() {

        if (instance == null) {

            instance = new AppPreferences();
            context = CustomApplication.getGlobalContext();
            pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();

        }
        return instance;

    }

    /**
     * Store user info in session once loggedIn
     *
     *
     * @param userInfo
     */
    public void setLoginInfo(String userInfo) {

        editor.putBoolean(IS_LOGGEDIN, true);
        editor.putString(USER_INFO, userInfo);
        editor.commit();
    }

    public void setCityData(String deviceToken){
        setPreferences(CITY_DATA, deviceToken);

    }

    public String getCityData(){
        return pref.getString(CITY_DATA, "");
    }

    public void setStateData(String deviceToken){
        setPreferences(STATE_DATA, deviceToken);

    }

    public String getStateData(){
        return pref.getString(STATE_DATA, "");
    }


    public void setIsAppKilled(Boolean status){
        editor.putBoolean(STATE_DATA, status);
        editor.commit();
    }

    public Boolean getIsAppKilled(){
        return pref.getBoolean(STATE_DATA, false);
    }



    public void setDeviceToken(String deviceToken){
        setPreferences(DEVICE_TOKEN, deviceToken);

    }

    public String getDeviceToken(){
        return pref.getString(DEVICE_TOKEN, "");
    }

    /**
     * Get user detail from session
     *
     * @return
     */
    public HashMap<String, String> getLoginInfo() {

        HashMap<String, String> user = new HashMap<String, String>();
        user.put(AUTH_TOKEN, pref.getString(AUTH_TOKEN, null));
        user.put(USER_INFO, pref.getString(USER_INFO, null));
        return user;

    }


    public JSONObject getUserInfo() {

        String profileInfo = pref.getString(USER_INFO, null);
        if(profileInfo != null) {

            try{
                JSONObject userInfo =  new JSONObject(profileInfo);
                return userInfo;
            }

            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;

    }

    public HashMap<String, Object> sendingInputParamBuyMedicine(){
        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        String profileInfo = pref.getString(USER_INFO, null);
        if(profileInfo != null) {

            try{
                JSONObject userInfo =  new JSONObject(profileInfo);
                inputParams.put("Patientid",userInfo.getString("PatientID"));
//                inputParams.put("PatientName",userInfo.getString("FirstName")+" "+userInfo.getString("LastName"));
//
//                inputParams.put("ClientID",userInfo.getString("ClientID"));
//                inputParams.put("PatientID",userInfo.getString("PatientID"));
//
//                inputParams.put("PatEmail",userInfo.getString("Email"));
//                inputParams.put("PatPhone",userInfo.getString("ContactNo"));
//
//                inputParams.put("Offset", "-330");
            }

            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return inputParams;

    }



    public HashMap<String, Object> sendingInputParam(){
        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        String profileInfo = pref.getString(USER_INFO, null);
        if(profileInfo != null) {

            try{
                JSONObject userInfo =  new JSONObject(profileInfo);
                inputParams.put("patientID",userInfo.getString("PatientID"));
                inputParams.put("PatientName",userInfo.getString("FirstName")+" "+userInfo.getString("LastName"));

                inputParams.put("ClientID",userInfo.getString("ClientID"));
                inputParams.put("PatientID",userInfo.getString("PatientID"));

                inputParams.put("PatEmail",userInfo.getString("Email"));
                inputParams.put("PatPhone",userInfo.getString("ContactNo"));

                inputParams.put("Offset", "-330");




            }

            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return inputParams;

    }

    public String getSiteURL() {

        String profileInfo = pref.getString(USER_INFO, null);
        String defaultURL = "";

        if(profileInfo != null) {

            try {
                JSONObject userInfo =  new JSONObject(profileInfo);
                if(!userInfo.isNull(SITE_URL)){
                    return (userInfo.getString(SITE_URL).length() > 0)? userInfo.getString(SITE_URL) : defaultURL;
                }
                return defaultURL;
            }

            catch (JSONException e){
                e.printStackTrace();
            }

        }
        return defaultURL;

    }

    public String getProfileName() {

        String profileInfo = pref.getString(USER_INFO, null);
        if(profileInfo != null) {
            try{
                JSONObject userInfo =  new JSONObject(profileInfo);
                if (userInfo.getString("full_name") != null) {
                    return userInfo.getString("full_name");
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

        }
        return Config.AppName;

    }

//    /**
//     * Check the user is loggedIn and redirect to Login screen
//     *
//     * @return
//     */
//    public boolean checkLogin() {
//
//        if (!this.isLoggedIn()) {
//
//            Intent authHome = new Intent(context, AuthHome.class);
//            context.startActivity(authHome);
//            Utilities.pushAnimation((Activity) context);
//            return false;
//
////            Intent loginActivity = new Intent(context, AuthHome.class);
////            loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            context.startActivity(loginActivity);
////            return false;
//
//        }
//        return true;
//
//    }

    /**
     * Check the user is loggedIn and redirect to Login screen
     *
     * @return
     */
    public boolean checkLogin(Context context) {

        if (!this.isLoggedIn()) {

            Intent authHome = new Intent(context, SignIn.class);
            context.startActivity(authHome);
            Utilities.pushAnimation((Activity) context);
            return false;

        }
        return true;

    }


    /**
     * Check the user is loggedIn or not
     *
     * @return
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGEDIN, false);
    }

    /**
     * For Localaization
     * @return
     */
    public String retrieveLanguage(){
        return pref.getString(LANGUAGE, "en");
    }

    /**
     * Store data in session
     *
     * @param key
     * @param value
     */
    public void setPreferences(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Get Data from session
     *
     * @param key
     * @return
     */
    public String getPreferences(String key) {
        return pref.getString(key, null);
    }

    /**
     * Get Access_Token
     *
     * @return
     */
    public String getAuthToken() {

        if (pref == null)
            return "";
        return pref.getString(AUTH_TOKEN, "Xpolor_api");

    }

    /**
     * Logout and clear the session
     */
    public void logout(Context context) {

        editor.putBoolean(IS_LOGGEDIN, false);
        editor.putString(AUTH_TOKEN, null);
        editor.putString(USER_INFO, null);
        editor.commit();

        //call this to clear the activities and open the dashboard
        Intent homeScreen = new Intent(context, SignIn.class);
        homeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(homeScreen);
    }


    // MARK : Application level preferences
    public void saveCategories(ArrayList<HashMap<String, Object>> inputHashMap) {

        Gson gson = new Gson();
        String categoriesInJSONForm = gson.toJson(inputHashMap);
        setPreferences(SAVED_CATEGORIES, categoriesInJSONForm);

    }

    public ArrayList<HashMap<String, Object>> retrieveCategories(){

        ArrayList<HashMap<String, Object>> categoriesInLocal = new ArrayList<HashMap<String, Object>>();
        try{

            String categoriesJSON = getPreferences(SAVED_CATEGORIES);
            JSONArray categoryItems = (categoriesJSON != null)? new JSONArray(categoriesJSON) : new JSONArray();

            for(int index = 0; index < categoryItems.length(); index++){

                JSONObject categoryItem = categoryItems.getJSONObject(index);
                HashMap<String, Object> item = JSONHandler.jsonObjectToHashMap(categoryItem);
                categoriesInLocal.add(item);

            }

        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<HashMap<String, Object>>();
        }
        return categoriesInLocal;

    }

    public void addToSelectedCategory(String categoryID){

        if(categoryID != null){

            ArrayList<String> savedCategories = getSelectedCategories();
            savedCategories = (savedCategories != null)? savedCategories : new ArrayList<String>();

            if(savedCategories.contains(categoryID)){
                savedCategories.remove(categoryID);
            }
            else{
                savedCategories.add(categoryID);
            }

            // save the changes in the shared prefs
            Gson gson = new Gson();
            setPreferences(SELECTED_CATEGORIES, gson.toJson(savedCategories));

        }

    }

    public void resetSelectedCategories(){
        setPreferences(SELECTED_CATEGORIES, "");
    }

    public ArrayList<String> getSelectedCategories() {

        Gson gson = new Gson();
        String json = getPreferences(SELECTED_CATEGORIES);
        Type type = new TypeToken<List<String>>() {}.getType();
        ArrayList<String> categoryList = gson.fromJson(json, type);
        categoryList = (categoryList != null)? categoryList : new ArrayList<String>();
        return categoryList;

    }

    public void setLocationDetails(String latitude, String longitude, String locationName) {
        setPreferences(SELECTED_LATTITUDE, latitude);
        setPreferences(SELECTED_LONGITUDE, longitude);
        setPreferences(SELECTED_ADDRESS, locationName);
    }

    public String getSelectedLatitude(){
        return pref.getString(SELECTED_LATTITUDE, "");
    }

    public String getSelectedLongitude(){
           return pref.getString(SELECTED_LONGITUDE, "");
    }

    public String getSelectedAddress(){
        return pref.getString(SELECTED_ADDRESS, "Choose your location");
    }

    public boolean isLocationRequested(){
        return  pref.getBoolean(LOCATION_REQUESTED, false);
    }

    public void setLocationRequested(boolean locationRequested){
        editor.putBoolean(LOCATION_REQUESTED, locationRequested);

    }



}
