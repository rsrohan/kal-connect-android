package com.kal.connect.customLibs.HTTP.GetPost;

import android.content.Context;

import org.json.JSONException;

/**
 * Created by lakeba_prabhu on 26/03/17.
 */

public interface APICallback {
    public void responseCallback(Context context, String response) throws JSONException;
}
