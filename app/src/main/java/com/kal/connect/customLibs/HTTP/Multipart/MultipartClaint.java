package com.kal.connect.customLibs.HTTP.Multipart;

import android.content.Context;
import android.os.AsyncTask;

import com.kal.connect.customLibs.HTTP.MultipartUtility;
import com.kal.connect.customLibs.customdialogbox.FlipProgressDialog;
import com.kal.connect.utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MultipartClaint extends AsyncTask<Void, Integer, String>
{
    String response = "";
    String TAG = getClass().getSimpleName();
    FlipProgressDialog pDialog;
    private Context context;
    MultipartUtility multipart;


    MultipartCallback responseCallBack;
    public MultipartClaint(Context context, MultipartUtility multipart,MultipartCallback responseCallBack){
        this.context = context;

        this.multipart = multipart;
        this.responseCallBack = responseCallBack;
    }
    protected void onPreExecute (){
        super.onPreExecute();
        pDialog = Utilities.showLoading(context);
    }

    protected String doInBackground(Void...arg0) {
        try {
            response = multipart.finish(); // response from server.
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(pDialog != null){
            pDialog.dismiss();
        }

        // Display Success Message
        if (Utilities.isValidResponse(context, response) != null) {

            JSONObject APIResponse = null;
            try {
                APIResponse = new JSONObject(response);
                responseCallBack.responseJsonCallback(context,APIResponse);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
