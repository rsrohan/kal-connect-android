package com.kal.connect.customLibs.HTTP.GetPost;

import android.content.Context;
import android.util.Log;

import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Utilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lakeba_prabhu on 26/03/17.
 * Reference : http://danielnugent.blogspot.in/
 */

public class HTTPClient {

    HttpURLConnection connection;
    URL apiURL;

    StringBuilder result;
    String apiResponse;
    StringBuilder inputParams;
    int timeout = 15000;
    public Context context = null;

    public String makeHttpRequest(String url, String method,
                                  HashMap<String, Object> params,HashMap<String, String> headerParams) {

        inputParams = buildRequestParams(params);


        try {

            if (method.equalsIgnoreCase("post")) {

                apiURL = new URL(url);
                connection = (HttpURLConnection) apiURL.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection = buildHeaderParams(connection, headerParams);
                // For Connection Time
                connection.setConnectTimeout(timeout);
                // For Reading Time
                connection.setReadTimeout(timeout);
                connection.connect();

                // DataOutPutStream => To send the input params in string format into http connection stream
                DataOutputStream write = new DataOutputStream(connection.getOutputStream());
                write.writeBytes(inputParams.toString());
                write.flush();
                write.close();

            } else if (method.equalsIgnoreCase("get")) {

                if (inputParams.length() != 0) {
                    url += "?" + inputParams.toString();
                }

                apiURL = new URL(url);
                connection = (HttpURLConnection) apiURL.openConnection();
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection = buildHeaderParams(connection,headerParams);
                // For Connection Time
                connection.setConnectTimeout(timeout);
                // For Reading Time
                connection.setReadTimeout(timeout);
                connection.connect();

            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream in = new BufferedInputStream(connection.getInputStream());

                // BufferedReader => Allows to Read data from HTTP connection's input stream
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                result = new StringBuilder();

                String eachLine;
                while ((eachLine = reader.readLine()) != null) {
                    result.append(eachLine);
                }
                Log.d("API Response", result.toString());


            } else {

                handleError(connection);
            }

        // Timeout Error
        } catch (SocketTimeoutException e) {

            e.printStackTrace();
            Utilities.showAlert(context, "Unauthorized access", false);

        // Un-Predictable Response with Error
        } catch (Exception e) {

            e.printStackTrace();
            Utilities.showAlert(context, "No response found!", false);

        } finally {
            connection.disconnect();
        }

        // Return APIs Response
        if (result != null && result.toString().length() > 0){
            apiResponse = result.toString();
        }

        return apiResponse;
    }

    /**
     * Build the Input parameters
     *
     * @param params
     * @return
     */
    private StringBuilder buildRequestParams(HashMap<String, Object> params) {

        StringBuilder apiParams = new StringBuilder();
        int loop = 0;
        for (String key : params.keySet()) {
            try {
                if (loop != 0) {
                    apiParams.append("&");
                }
                apiParams.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key).toString(), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            loop++;
        }
        return apiParams;

    }


    /**
     * Add Required Header params
     *
     * @param conn
     * @return
     */
    private HttpURLConnection buildHeaderParams(HttpURLConnection conn, HashMap<String, String> headerParams) {

        // Char-Set
        conn.setRequestProperty("Accept-Charset", "UTF-8");

        // Auth-Token
        conn.setRequestProperty(AppPreferences.AUTH_TOKEN, AppPreferences.getInstance().getAuthToken());

        // App-Language
        String deviceLanguage = (AppPreferences.getInstance().retrieveLanguage().equalsIgnoreCase("en"))? "en-US" : AppPreferences.getInstance().retrieveLanguage();
        if(deviceLanguage != null){
            conn.setRequestProperty("Accept-Language", deviceLanguage);
        }

        if(headerParams != null){
            for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                conn.setRequestProperty(key, value);
            }
        }

        return conn;
    }

    public void handleError(HttpURLConnection conn) {

        try {

            InputStream error = new BufferedInputStream(connection.getErrorStream());

            // BufferedReader => Allows to Read data from HTTP connection's input stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(error));
            StringBuilder errorResult = new StringBuilder();

            String eachLine;
            while ((eachLine = reader.readLine()) != null) {
                errorResult.append(eachLine);
            }
            Log.d("API Error Response", errorResult.toString());


            if (conn.getResponseCode() == 401) {
                Utilities.showAlert(context, "Unauthorized access", false);
            } else if (conn.getResponseCode() == 404) {
                Utilities.showAlert(context, "API Not found", false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
