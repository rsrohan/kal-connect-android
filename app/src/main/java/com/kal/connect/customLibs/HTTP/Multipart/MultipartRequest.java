package com.kal.connect.customLibs.HTTP.Multipart;

import android.content.Context;
import android.util.Log;

import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lakeba_prabhu on 27/03/17.
 */

public class MultipartRequest {

    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset = "UTF-8";
    private OutputStream outputStream;
    private PrintWriter writer;

    private int totalBytesLength = 0;
    MultipartCallback progressCallback;
    Context context;

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param callback
     * @throws IOException
     */
    public MultipartRequest(String requestURL, Context mContext, MultipartCallback callback)
            throws IOException {
        this.progressCallback = callback;
        this.context = mContext;

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
        httpConn.setRequestProperty("Test", "Bonjour");
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {

        totalBytesLength += (int) uploadFile.length();

        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        int progress = 0;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);

            //update progress bar
            progress += bytesRead;
            int uploadPercentage = (int) ((progress * 100) / totalBytesLength);
            Log.d("Uploading....", "" + uploadPercentage + "%");
            this.progressCallback.progressCallback(uploadPercentage);

        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws IOException {
        StringBuffer response = new StringBuffer();

        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {

            handleError(httpConn);
            System.out.println("Response Code : " + status);

        }

        return response.toString();
    }

    /**
     * Build the header param
     *
     * @param conn
     * @return
     */
    private HttpURLConnection buildHeaderParams(HttpURLConnection conn) {

        // Char-Set
        conn.setRequestProperty("Accept-Charset", "UTF-8");

        // Auth-Token
        conn.setRequestProperty(AppPreferences.AUTH_TOKEN, AppPreferences.getInstance().getAuthToken());

        // App-Language
        String deviceLanguage = (AppPreferences.getInstance().retrieveLanguage().equalsIgnoreCase("en"))? "en-US" : AppPreferences.getInstance().retrieveLanguage();
        if(deviceLanguage != null){
            conn.setRequestProperty("Accept-Language", deviceLanguage);
        }

        return conn;

    }

    public void handleError(HttpURLConnection conn) {

        try {

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

