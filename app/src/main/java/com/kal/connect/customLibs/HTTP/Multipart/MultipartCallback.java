package com.kal.connect.customLibs.HTTP.Multipart;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lakeba_prabhu on 27/03/17.
 */

public interface MultipartCallback {
    public void responseCallback(Context context, String response) throws JSONException;
    public void responseJsonCallback(Context context, JSONObject response) throws JSONException;
    public void progressCallback(Integer percentage);
}
