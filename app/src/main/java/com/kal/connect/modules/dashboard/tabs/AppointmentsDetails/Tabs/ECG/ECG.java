package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.ECG;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kal.connect.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kal.connect.customLibs.downloadService.DirectoryHelper;
import com.kal.connect.customLibs.downloadService.DownloadFileService;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Records.RecordsAdapter;
import com.kal.connect.utilities.AppComponents;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ECG  extends Fragment implements View.OnClickListener, RecordsAdapter.ItemClickListener {

    // MARK : UIElements
    View view;
    private ArrayList<HashMap<String, Object>> dataItems = new ArrayList<HashMap<String, Object>>();
    RecyclerView vwRecords;
    ECGAdapter dataAdapter = null;

    // Progress Dialog
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;



    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.records, container, false);
        buildUI();
        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        loadRecords();
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

        dataAdapter = new ECGAdapter(dataItems, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        vwRecords.setNestedScrollingEnabled(false);
        vwRecords.setLayoutManager(mLayoutManager);
        vwRecords.setItemAnimator(new DefaultItemAnimator());
        vwRecords.setAdapter(dataAdapter);

        if (getActivity() != null)
            AppComponents.reloadDataWithEmptyHint(vwRecords, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_appointments_found));

        dataAdapter.setOnItemClickListener(new ECGAdapter.ItemClickListener() {

            @Override
            public void onItemClick(int position, View v) {




                try {

                    HashMap<String, Object> selectedItem = dataItems.get(position);

                    final JSONObject fileInfo = new JSONObject();
                    fileInfo.put(DownloadFileService.FILE_NAME_KEY,selectedItem.get("recordName").toString());
                    // dummy data
//                    fileInfo.put(DownloadFileService.FILE_URL_KEY, "https://imgix.bustle.com/uploads/image/2019/1/31/501c1ae6-5e39-4c4e-8394-713af0fd012b-150417-news-batman-superman.jpg?w=1020&h=574&fit=crop&crop=faces&auto=format&q=70");
                    fileInfo.put(DownloadFileService.FILE_URL_KEY, selectedItem.get("recordImgPath"));

                    final String fileName = selectedItem.get("recordName").toString();




                    // Step 1: Build File properties

                    Dexter.withActivity(getActivity())
                            .withPermissions(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE

                            ).withListener(new MultiplePermissionsListener() {
                        @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */
                            getActivity().startService(DownloadFileService.getDownloadService(getActivity(), fileInfo.toString(), DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));

                            // Step 3: Show the detail in alertview
                            Utilities.showAlert(getActivity(), "Downloading "+ fileName, false);

                        }
                        @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                    }).check();

                }
                catch (Exception e){
                    e.printStackTrace();
                }

//                    String fileToDownload = "Record file name "+ selectedItem.get("recordImgPath").toString();

            }

        });

    }

    public void openDocument(){

    }

    // MARK : API
    private void loadRecords() {

        // Show loading only at first time
        Boolean showLoading = dataItems.size() == 0;
        dataItems.clear();

        try{
            JSONArray patientRecords = GlobValues.getAppointmentCompleteDetails().getJSONArray("ECG");

            // Make API Call here!
            for (int loop = 0; loop < patientRecords.length(); loop++) {

                JSONObject singleObj = patientRecords.getJSONObject(loop);
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("recordName", Utilities.dateRT(singleObj.getString("CreatedDate")) + (singleObj.getString("Filedescription").isEmpty()?"" : " : "+ singleObj.getString("Filedescription")));
                item.put("recordImgPath", singleObj.getString("File"));
                // Add item one by one
                dataItems.add(item);

            }


        }catch (Exception e){

        }


        AppComponents.reloadDataWithEmptyHint(vwRecords, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_data_found));

    }


    @Override
    public void onItemClick(int position, View v, int viewID) {
        HashMap<String, Object> item = dataItems.get(position);
        Utilities.showLoading(getContext());
    }


}