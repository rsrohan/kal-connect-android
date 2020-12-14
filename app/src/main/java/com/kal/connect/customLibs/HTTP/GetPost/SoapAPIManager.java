package com.kal.connect.customLibs.HTTP.GetPost;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.kal.connect.appconstants.APIWebServiceConstants;
import com.kal.connect.customdialogbox.FlipProgressDialog;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SoapAPIManager extends AsyncTask<String, String, String> {

    Context context;
    Boolean showLoader = true;
    FlipProgressDialog pDialog;
    APICallback apiCallback;
    HashMap<String, Object> paramsToSend;
    String response;

    public SoapAPIManager(Context context, HashMap<String, Object> paramsToSend, APICallback apiCallback, Boolean showLoader) {
        this.context = context;
        this.apiCallback = apiCallback;
        this.showLoader = showLoader;
        this.paramsToSend = paramsToSend;
        setupDefaultParams();
    }

    void setupDefaultParams(){
        try{
            this.paramsToSend.put("LanguageIndicator",AppPreferences.getInstance().retrieveLanguage());
        }catch (Exception e){}
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.showLoader) {
            pDialog = Utilities.showLoading(this.context);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (pDialog != null){
            try{
                pDialog.dismiss();

            }catch (Exception e){}

        }

        try {
            apiCallback.responseCallback(context, response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String doInBackground(String... params) {

        String requestUrlEND = params[0];
        String requestMethodName = params[1];
        String requestType = params[2];

        //Build HTTPClient
        HTTPClient myHttpClient = new HTTPClient();
        myHttpClient.context = this.context;

        //set the Response
        Log.d("API URL and Methos", requestUrlEND+"/"+requestMethodName);
        Log.d("Input Params", "" + paramsToSend);

        //make API call        //Check interne connection
        if (Utilities.isNetworkAvailable(this.context)) {
            JSONObject inputParams = new JSONObject(paramsToSend);

            return response = APIWebServiceConstants.invokeWebservice(inputParams.toString(),requestUrlEND,requestMethodName);
        }

        return response;

    }

}
