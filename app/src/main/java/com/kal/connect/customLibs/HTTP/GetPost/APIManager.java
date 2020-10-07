package com.kal.connect.customLibs.HTTP.GetPost;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kal.connect.utilities.Utilities;

import org.json.JSONException;

import java.util.HashMap;

/**
 * Created by lakeba_prabhu on 26/03/17.
 * http://danielnugent.blogspot.in/
 */

public class APIManager extends AsyncTask<String, String, String> {

    Context context;
    Boolean showLoader = true;
    ProgressDialog pDialog;
    APICallback apiCallback;
    HashMap<String, Object> paramsToSend;
    HashMap<String, String> headers;
    String response;

    public APIManager(Context context, HashMap<String, Object> paramsToSend, APICallback apiCallback, HashMap<String, String> headers, Boolean showLoader) {
        this.context = context;
        this.apiCallback = apiCallback;
        this.showLoader = showLoader;
        this.paramsToSend = paramsToSend;
        this.headers = headers;
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
        if (pDialog != null)
            pDialog.dismiss();

        try {
            apiCallback.responseCallback(context, response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String doInBackground(String... params) {

        String requestUrl = params[0];
        String requestType = params[1];

        //Build HTTPClient
        HTTPClient myHttpClient = new HTTPClient();
        myHttpClient.context = this.context;

        //set the Response
        Log.d("API URL", requestUrl);
        Log.d("Input Params", "" + paramsToSend);

        //make API call        //Check interne connection
        if (Utilities.isNetworkAvailable(this.context)) {
            return response = myHttpClient.makeHttpRequest(requestUrl, requestType, paramsToSend, headers);
        }
        return response;

    }

}
