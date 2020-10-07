package com.kal.connect.customLibs.HTTP.Multipart;

/**
 * Created by lakeba_prabhu on 27/03/17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.kal.connect.utilities.Utilities;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lakeba_prabhu on 27/03/17.
 */

public class MultipartManager extends AsyncTask<String, String, String> {

    Context context;
    ProgressDialog pDialog;
    MultipartCallback multipartCallback;
    ArrayList<MultipartModel> paramsToSend;
    String response = "";
    Boolean showLoader = true;
    MultipartRequest multipart = null;

    public MultipartManager(Context context, ArrayList<MultipartModel> paramsToSend, MultipartCallback apiCallback, Boolean showLoader) {
        this.context = context;
        this.multipartCallback = apiCallback;
        this.paramsToSend = paramsToSend;
        this.showLoader = showLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.showLoader) {
            pDialog = Utilities.showLoading(this.context);
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (pDialog != null)
            pDialog.dismiss();

        try {
            this.multipartCallback.responseCallback(context, response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {

        String requestUrl = params[0];
        try {
            //launch file upload
            if (Utilities.isNetworkAvailable(this.context)) {

                System.out.println("\n API URL : "+ requestUrl);
                System.out.println("\n Input Params : "+ paramsToSend);

                multipart = new MultipartRequest(requestUrl, context, this.multipartCallback);

                // Attach Fields
                for (MultipartModel param : paramsToSend) {
                    if(!param.isFile){
                        multipart.addFormField(param.key, param.value);
                    }
                }

                // Attach File
                for (MultipartModel param : paramsToSend) {
                    if(param.isFile){
                        multipart.addFilePart(param.key, (File) param.file);
                    }
                }

                response = multipart.finish();
                System.out.println("\n API Response : \n "+ response);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

         return response;

    }

}
