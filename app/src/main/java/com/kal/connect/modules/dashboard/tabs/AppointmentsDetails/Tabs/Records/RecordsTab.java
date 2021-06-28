package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Records;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.downloadService.DirectoryHelper;
import com.kal.connect.customLibs.downloadService.DownloadFileService;
import com.kal.connect.modules.dashboard.PDFViewerActivity;
import com.kal.connect.utilities.AppComponents;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.androidannotations.api.rest.MediaType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RecordsTab extends Fragment implements View.OnClickListener {

    // MARK : UIElements
    View view;
    private ArrayList<HashMap<String, Object>> dataItems = new ArrayList<HashMap<String, Object>>();
    RecyclerView vwRecords;
    RecordsAdapter dataAdapter = null;

    static String TAG = "RecordsTag";
    @BindView(R.id.update)
    Button updateBtn;


    /* access modifiers changed from: 0000 */
    @OnClick({R.id.update})
    public void update() {
        uploadFilesToServer();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({R.id.upload})
    public void upload() {
        // Step 1: Build File properties

        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose a file..."), 1001);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                /* ... */
                Log.v("", "onPermissionRationaleShouldBeShown.....");
                token.continuePermissionRequest();

            }
        }).check();


    }

    // Progress Dialog
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;


    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.records, container, false);
        buildUI();
        ButterKnife.bind(this, view);
        try {
            loadRecords(GlobValues.getAppointmentCompleteDetails().getJSONArray("PatRec"));
        } catch (Exception e) {

        }

        return view;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == -1) {

//            Uri uri = data.getData();
//            String uriString = uri.toString();
//            File myFile = new File(uriString);
//            String path = myFile.getAbsolutePath();
//            String displayName = null;
//
//            if (uriString.startsWith("content://")) {
//                Cursor cursor = null;
//                try {
//                    cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
//                    if (cursor != null && cursor.moveToFirst()) {
//                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                    }
//                } finally {
//                    cursor.close();
//                }
//            } else if (uriString.startsWith("file://")) {
//                displayName = myFile.getName();
//            }

//            String filePath = data.getData().getPath();

            String filePath = /*myFile.getAbsolutePath();//*/RealPathUtil.getRealPath(getContext(), data.getData());
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            HashMap<String, Object> item = new HashMap<>();

            Log.e(TAG, fileName+"onActivityResult: "+filePath );

            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Kerala Ayurveda");
            alert.setMessage("Enter record details");
            alert.setCancelable(false);

            final EditText input = new EditText(getContext());
            input.setTextColor(Color.BLACK);
            alert.setView(input);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString().trim();
                    // Do something with value!
                    if (value.isEmpty()) {
                        item.put("recordName", fileName);
                    } else {
                        item.put("recordName", value);
                    }


                    item.put("recordImgPath", filePath);
                    item.put("oldFile", Boolean.FALSE);
                    dataItems.add(item);
                    AppComponents.reloadDataWithEmptyHint(vwRecords, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_data_found));
                    showUploadOptions();
                }

            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }
    }

    public void showUploadOptions() {
        boolean shouldVisible = false;
        int i = 0;
        while (true) {
            if (i >= this.dataItems.size()) {
                break;
            } else if (!((Boolean) ((HashMap) this.dataItems.get(i)).get("oldFile")).booleanValue()) {
                shouldVisible = true;
                break;
            } else {
                i++;
            }
        }
        if (shouldVisible) {
            this.updateBtn.setVisibility(View.VISIBLE);
        } else {
            this.updateBtn.setVisibility(View.GONE);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
//        loadRecords();
    }

    // MARK : UIActions
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }

    // MARK : Instance Methods
    private void buildUI() {

        vwRecords = (RecyclerView) view.findViewById(R.id.recordRecycler);
        buildListView();

    }


    private void buildListView() {

        dataAdapter = new RecordsAdapter(dataItems, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        vwRecords.setNestedScrollingEnabled(false);
        vwRecords.setLayoutManager(mLayoutManager);
        vwRecords.setItemAnimator(new DefaultItemAnimator());
        vwRecords.setAdapter(dataAdapter);

        if (getActivity() != null)
            AppComponents.reloadDataWithEmptyHint(vwRecords, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_appointments_found));

        dataAdapter.setOnItemClickListener(new RecordsAdapter.ItemClickListener() {

            @Override
            public void onItemClick(int position, View v, int viewID) {


                try {

                    HashMap<String, Object> selectedItem = dataItems.get(position);

                    if (viewID == R.id.btnDelete) {
                        RecordsTab.this.dataItems.remove(position);
                        AppComponents.reloadDataWithEmptyHint(RecordsTab.this.vwRecords, RecordsTab.this.dataAdapter, RecordsTab.this.dataItems, RecordsTab.this.getActivity().getResources().getString(R.string.no_data_found));
                        RecordsTab.this.showUploadOptions();
                        return;
                    }
                    if (!((Boolean) selectedItem.get("oldFile")).booleanValue()) {
                        openFile(getContext(), new File(selectedItem.get("recordImgPath").toString()));

                        return;
                    }

                    final JSONObject fileInfo = new JSONObject();

                    String extension = selectedItem.get("recordImgPath").toString().substring(selectedItem.get("recordImgPath").toString().lastIndexOf(".") + 1).toLowerCase();
                    if (extension.equals("pdf")) {
                        openRemoteFile(selectedItem);
                        return;
                    }
                    if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")) {

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedItem.get("recordImgPath").toString()));
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cdn.pixabay.com/photo/2016/11/09/16/24/virus-1812092__480.jpg"));
//                        intent.setType("image/*");
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return;
                    }


                    fileInfo.put(DownloadFileService.FILE_NAME_KEY, selectedItem.get("recordName").toString());
                    // dummy data
//                    fileInfo.put(DownloadFileService.FILE_URL_KEY, "https://imgix.bustle.com/uploads/image/2019/1/31/501c1ae6-5e39-4c4e-8394-713af0fd012b-150417-news-batman-superman.jpg?w=1020&h=574&fit=crop&crop=faces&auto=format&q=70");
                    fileInfo.put(DownloadFileService.FILE_URL_KEY, selectedItem.get("recordImgPath"));

                    final String fileName = selectedItem.get("recordName").toString();


                    Dexter.withActivity(getActivity())
                            .withPermissions(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE

                            ).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */


//                            getActivity().startService(DownloadFileService.getDownloadService(getActivity(), fileInfo.toString(), DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));
//
//                            // Step 3: Show the detail in alertview
//                            Utilities.showAlert(getActivity(), "Downloading "+ fileName, false);

                            getActivity().startService(DownloadFileService.getDownloadService(getActivity(), fileInfo.toString(), DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));
                            Utilities.showAlert(getActivity(), "Downloading " + fileName, false);

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                    }).check();


                    // Step 1: Build File properties

//                    Dexter.withActivity(getActivity())
//                            .withPermissions(
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                    Manifest.permission.READ_EXTERNAL_STORAGE
//
//                            ).withListener(new MultiplePermissionsListener() {
//                        @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */
//                            getActivity().startService(DownloadFileService.getDownloadService(getActivity(), fileInfo.toString(), DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));
//
//                            // Step 3: Show the detail in alertview
//                            Utilities.showAlert(getActivity(), "Downloading "+ fileName, false);
//
//                        }
//                        @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
//                    }).check();

                } catch (Exception e) {
                    e.printStackTrace();
                }

//                    String fileToDownload = "Record file name "+ selectedItem.get("recordImgPath").toString();

            }

        });

    }


    void openRemoteFile(HashMap<String, Object> selectedItem) {
        try {


//                    final JSONObject fileInfo = new JSONObject();
//                    fileInfo.put(DownloadFileService.FILE_NAME_KEY,selectedItem.get("prescription").toString());
//                    // dummy data
////                    fileInfo.put(DownloadFileService.FILE_URL_KEY, "https://imgix.bustle.com/uploads/image/2019/1/31/501c1ae6-5e39-4c4e-8394-713af0fd012b-150417-news-batman-superman.jpg?w=1020&h=574&fit=crop&crop=faces&auto=format&q=70");
//                    fileInfo.put(DownloadFileService.FILE_URL_KEY, selectedItem.get("recordImgPath"));
//                    fileInfo.put(DownloadFileService.FILE_URL_KEY, selectedItem.get("recordImgPath"));
//
//                    final String fileName = selectedItem.get("prescription").toString();

            // Step 1: Build File properties

            Dexter.withActivity(getActivity())
                    .withPermissions(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE

                    ).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */


//                            getActivity().startService(DownloadFileService.getDownloadService(getActivity(), fileInfo.toString(), DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));
//
//                            // Step 3: Show the detail in alertview
//                            Utilities.showAlert(getActivity(), "Downloading "+ fileName, false);

                    Intent i = new Intent(getActivity(), PDFViewerActivity.class);

                    i.putExtra("Url", selectedItem.get("recordImgPath").toString());
                    startActivity(i);

                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
            }).check();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MARK : API
    private void loadRecords(JSONArray patientRecords) {

        // Show loading only at first time
        Boolean showLoading = dataItems.size() == 0;
        dataItems.clear();

        try {
//             = GlobValues.getAppointmentCompleteDetails().getJSONArray("PatRec");

            // Make API Call here!
            for (int loop = 0; loop < patientRecords.length(); loop++) {

                JSONObject singleObj = patientRecords.getJSONObject(loop);
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("recordName", Utilities.dateRT(singleObj.getString("CreatedDate")) + (singleObj.getString("Filedescription").isEmpty() ? "" : " : " + singleObj.getString("Filedescription")));
                item.put("recordImgPath", singleObj.getString("File"));
                item.put("oldFile", true);
                // Add item one by one
                dataItems.add(item);

            }


        } catch (Exception e) {

        }


        AppComponents.reloadDataWithEmptyHint(vwRecords, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_data_found));

    }

    void createRecordAfterUploadedData(JsonArray response) {

    }

//    @Override
//    public void onItemClick(int position, View v, int viewID) {
//        HashMap<String, Object> item = dataItems.get(position);
//        Utilities.showLoading(getContext());
//    }

    public static void openFile(Context context, File url) throws IOException {
        Uri uri = Uri.fromFile(url);

//        final Uri data = FileProviderShare.getUriForFile(context, "myprovider", url);


//        context.grantUriPermission(context.getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = new Intent("android.intent.action.VIEW");
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            intent.setDataAndType(uri, MediaType.IMAGE_GIF);
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            intent.setDataAndType(uri, MediaType.IMAGE_JPEG);
        } else if (url.toString().contains(".txt")) {
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, MediaType.ALL);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void uploadFilesToServer() {
        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        try {
            JSONArray semiAnalysor = GlobValues.getAppointmentCompleteDetails().getJSONArray("Vitals");

            JSONObject data = semiAnalysor.getJSONObject(0);

            inputParams.put("KIOSKID", data.getString("KioskId"));
            inputParams.put("ComplaintID", data.getString("ComplaintID"));
            inputParams.put("PatientID", data.getString("PatientID"));

            JSONArray misc = new JSONArray();
            JSONArray fileNotes = new JSONArray();

            for (int i = 0; i < dataItems.size(); i++) {
                HashMap<String, Object> dataItem = dataItems.get(i);
                if (!(boolean) dataItem.get("oldFile")) {

                    File file = new File(dataItem.get("recordImgPath").toString());
                    int size = (int) file.length();
                    byte[] bytes = new byte[size];
                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                        buf.read(bytes, 0, bytes.length);
                        buf.close();
                        misc.put(Base64.encodeToString(bytes, Base64.DEFAULT));
                        fileNotes.put(dataItem.get("recordName").toString());

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            inputParams.put("MiscDocuments", misc);
            inputParams.put("FileNotes", fileNotes);

        } catch (Exception e) {

        }


//Utilities.getDefaultAPIParams()
        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        loadRecords(responseAry);

                    }

                } catch (Exception e) {
                }
            }
        }, true);

        String[] url = {Config.WEB_Services1, Config.UPLOAD_FILES, "POST"};

        if (Utilities.isNetworkAvailable(getContext())) {
            apiManager.execute(url);
        } else {

        }

    }
}