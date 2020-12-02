package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Prescription;

import android.Manifest;
import android.content.Intent;
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
import com.kal.connect.modules.dashboard.PDFViewerActivity;
import com.kal.connect.utilities.AppComponents;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrescriptionTab extends Fragment implements View.OnClickListener, PrescriptionAdapter.ItemClickListener {

    // MARK : UIElements
    View view;
    private ArrayList<HashMap<String, Object>> dataItems = new ArrayList<HashMap<String, Object>>();
    RecyclerView vwPrescrition;
    PrescriptionAdapter dataAdapter = null;

    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.prescription, container, false);
        buildUI();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // MARK : UIActions
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }

    // MARK : Instance Methods
    private void buildUI() {

        vwPrescrition = (RecyclerView) view.findViewById(R.id.prescriptionRecycler);
        buildListView();

    }

    private void buildListView() {

        dataAdapter = new PrescriptionAdapter(dataItems, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        vwPrescrition.setNestedScrollingEnabled(false);
        vwPrescrition.setLayoutManager(mLayoutManager);
        vwPrescrition.setItemAnimator(new DefaultItemAnimator());
        vwPrescrition.setAdapter(dataAdapter);

        if (getActivity() != null)
            AppComponents.reloadDataWithEmptyHint(vwPrescrition, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_prescrription_found));

        dataAdapter.setOnItemClickListener(new PrescriptionAdapter.ItemClickListener() {

            @Override
            public void onItemClick(int position, View v) {
                try {

                    HashMap<String, Object> selectedItem = dataItems.get(position);



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
                        @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */


//                            getActivity().startService(DownloadFileService.getDownloadService(getActivity(), fileInfo.toString(), DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));
//
//                            // Step 3: Show the detail in alertview
//                            Utilities.showAlert(getActivity(), "Downloading "+ fileName, false);

                            Intent i = new Intent(getActivity(), PDFViewerActivity.class);

                            i.putExtra("Url", selectedItem.get("recordImgPath").toString());
                            startActivity(i);

                        }
                        @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                    }).check();

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

        });

        loadPrescriptions();

    }

    // MARK : API
    private void loadPrescriptions() {

        // Show loading only at first time
        Boolean showLoading = dataItems.size() == 0;
        dataItems.clear();

//        HashMap<String, Object> sampleItem = new HashMap<String, Object>();
//        sampleItem.put("prescription", Utilities.getCurrentDate("dd/mm/yyyyy"));
//        sampleItem.put("prescription", "Sample File");
//        sampleItem.put("recordImgPath", "https://www.medi360.in/Medi360PhyDataPath//29060/Patient_4/38080/Prescription/38080_40410.pdf");
//        dataItems.add(sampleItem);



        // Make API Call here!
        try{
            JSONArray ecgRecords = GlobValues.getAppointmentCompleteDetails().getJSONArray("Prescription");
            for (int loop = 0; loop < ecgRecords.length(); loop++) {

                JSONObject singleObj = ecgRecords.getJSONObject(loop);
                HashMap<String, Object> item = new HashMap<String, Object>();

                String createdDate = Utilities.dateRT(singleObj.getString("CreatedDate"));
                createdDate = Utilities.changeStringFormat(createdDate,"yyyy-mm-dd","dd/mm/yyyy");

                item.put("prescription", createdDate);
                item.put("diagnostics", singleObj.getString("DiagnosticReport").isEmpty()?"No Data":singleObj.getString("DiagnosticReport"));
                item.put("recordImgPath", singleObj.getString("File"));
                // Add item one by one
                dataItems.add(item);

            }




        }catch (Exception e){

        }



        AppComponents.reloadDataWithEmptyHint(vwPrescrition, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_prescrription_found));

    }

    @Override
    public void onItemClick(int position, View v) {

    }
}