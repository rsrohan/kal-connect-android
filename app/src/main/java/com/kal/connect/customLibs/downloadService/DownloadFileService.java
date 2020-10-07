package com.kal.connect.customLibs.downloadService;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class DownloadFileService extends IntentService {

    public static String FILE_NAME_KEY = "FILE_NAME";
    public static String FILE_URL_KEY = "FILE_URL";

    private static final String DOWNLOAD_PATH = "com.patientapp.androiddownloadmanager_DownloadFileService_Download_path";
    private static final String DESTINATION_PATH = "com.patientapp.androiddownloadmanager_DownloadFileService_Destination_path";

    public DownloadFileService() {
        super("DownloadFileService");
    }

    public static Intent getDownloadService(final @NonNull Context callingClassContext, final @NonNull String downloadPath, final @NonNull String destinationPath) {
        return new Intent(callingClassContext, DownloadFileService.class)
                .putExtra(DOWNLOAD_PATH, downloadPath)
                .putExtra(DESTINATION_PATH, destinationPath);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String downloadPath = intent.getStringExtra(DOWNLOAD_PATH);
        String destinationPath = intent.getStringExtra(DESTINATION_PATH);
        startDownload(downloadPath, destinationPath);
    }

    private void startDownload(String fileInfo, String destinationPath) {

        try{
            // parse the file metat data
            JSONObject fileMetaData = new JSONObject(fileInfo);
            String fileName = fileMetaData.getString(FILE_NAME_KEY);
            String fileURL = fileMetaData.getString(FILE_URL_KEY);

            Uri uri = Uri.parse(fileURL);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
            request.setTitle("Downloading " + fileName); // Title for notification.
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(destinationPath, uri.getLastPathSegment());  // Storage directory path
            ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading

        }
        catch (JSONException e){
            e.printStackTrace();
        }



    }
}
