package com.kal.connect.modules.dashboard.AppointmentsDetails;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.core.widget.SwipeRefreshLayout;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.google.gson.JsonObject;
import com.kal.connect.R;
import com.kal.connect.adapters.AppointmentsAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.utilities.AppComponents;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointmentsFragment extends Fragment implements View.OnClickListener {

    // MARK : UIElements
    View view;
    private ArrayList<HashMap<String, Object>> dataItems = new ArrayList<HashMap<String, Object>>();
    RecyclerView vwAppointments;
    AppointmentsAdapter dataAdapter = null;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    // MARK : Lifecycle
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.appointments, container, false);
        ButterKnife.bind(this, view);
        buildUI();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
//        loadAppointments();
    }

    // MARK : Instance Methods
    private void buildUI() {

        vwAppointments = (RecyclerView) view.findViewById(R.id.appointmentsView);
        buildListView();
        getAppointments();

        mSwipeRefreshLayout.setColorSchemeColors(Utilities.getColor(getContext(), R.color.colorPrimary), Utilities.getColor(getContext(), R.color.colorPrimaryDark), Utilities.getColor(getContext(), R.color.colorPrimary));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getAppointments();
            }
        });

    }

    private void buildListView() {

        dataAdapter = new AppointmentsAdapter(dataItems, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        vwAppointments.setNestedScrollingEnabled(false);
        vwAppointments.setLayoutManager(mLayoutManager);
        vwAppointments.setItemAnimator(new DefaultItemAnimator());
        vwAppointments.setAdapter(dataAdapter);


        if (getActivity() != null)
            AppComponents.reloadDataWithEmptyHint(vwAppointments, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_appointments_found));

//        dataAdapter.setOnItemClickListener(new AppointmentsAdapter.ItemClickListener() {
//
//            @Override
//            public void onItemClick(int position, View v) {
//
//                HashMap<String, Object> selectedItem = dataItems.get(position);
//                if (selectedItem.get("appointmentId") != null) {
//                    GlobValues.getInstance().setSelectedAppointment(selectedItem.get("appointmentId").toString());
//                    GlobValues.getInstance().setSelectedAppointmentData(selectedItem);
//                    getAppointmentsDetails();
//                }
//
//            }
//
//        });

    }

    // MARK : UIActions
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }

    // MARK : API
    private void loadAppointments(JSONArray appointmentAry) {

        // Show loading only at first time
        Boolean showLoading = dataItems.size() == 0;
        dataItems.clear();

        // Make API Call here!
        for (int loop = 0; loop < appointmentAry.length(); loop++) {
            try{
                JSONObject singleObj = appointmentAry.getJSONObject(loop);
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("appointmentId", singleObj.getString("AppointmentID"));
                item.put("ComplaintID", singleObj.getString("ComplaintID"));
                item.put("doctorId", singleObj.getString("SpecialistID"));
                item.put("doctorName", singleObj.getString("SpecialistName"));
                item.put("qualification", singleObj.getString("Qualification"));
                item.put("time", singleObj.getString("AppointmentTime"));
                item.put("date", singleObj.getString("AppointmentDate"));
                item.put("cancelstatus",singleObj.getString("cancelstatus"));
//                item.put("appointmentData", Utilities.dateRT(singleObj.getString("AppointmentDate") )+ singleObj.getString("AppointmentTime"));

                String appointmentDate = (singleObj.getString("AppointmentDate") != null)? singleObj.getString("AppointmentDate") : "";
                if(!appointmentDate.isEmpty())
                {
                    appointmentDate = Utilities.changeStringFormat(Utilities.dateRT(singleObj.getString("AppointmentDate") ),"yyyy-mm-dd","dd/mm/yyyy");
                    appointmentDate = appointmentDate + " " + singleObj.getString("AppointmentTime");
                }

                item.put("appointmentDate", appointmentDate);
                item.put("location", "Bangalore");
                item.put("consultationMode", singleObj.getString("Consultationmode").toLowerCase().contains("video")?1:2); // 1 -> Video Conferrece | 2 - Kiosk Center
                item.put("status", singleObj.getString("AppStatusDetails")); // Status of the appointment
                item.put("technicianStatus", singleObj.has("TechnicianCurStatus")?singleObj.getString("TechnicianCurStatus"):"Status"); // 1-> Technician Accepted | 2 -> Technicial Start Trip
                item.put("technicianRequired", singleObj.getString("Technician").equals("1")?true:false);
                item.put("complaints", singleObj.getString("PresentComplaint"));

                List<String> items = Arrays.asList(singleObj.getString("PresentComplaint").split("\\s*,\\s*"));
                ArrayList<HashMap<String, Object>> symptoms = new ArrayList<HashMap<String, Object>>();
                for(int i=0;i<items.size();i++){
                    // Symptoms
                    HashMap<String, Object> symptom = new HashMap<String, Object>();
                    symptom.put("title", items.get(i));
                    symptom.put("id", item.get(i));
                    symptoms.add(symptom);

                }
                item.put("symptoms", symptoms);
                // Add item one by one
                dataItems.add(item);
            }catch (Exception e){
                e.printStackTrace();

            }
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try{AppComponents.reloadDataWithEmptyHint(vwAppointments, dataAdapter, dataItems, getActivity().getResources().getString(R.string.no_appointments_found));}catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }
    void getAppointments(){
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    JSONArray responseAry = new JSONArray(response);
                    if(responseAry.length()>0){
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if(commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1){
                            if(commonDataInfo.has("Message") && commonDataInfo.getString("Message").isEmpty()){
                                Utilities.showAlert(getContext(),commonDataInfo.getString("Message"),false);
                            }else{
                                Utilities.showAlert(getContext(),"Please check again!",false);
                            }
                            return;

                        }
                        loadAppointments(responseAry);

                    }
                }catch (Exception e){

                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },true);
        String[] url = {Config.WEB_Services1,Config.GET_APPOINTMENTS_LIST,"POST"};

        if (Utilities.isNetworkAvailable(getContext())) {
            apiManager.execute(url);
        }else{
            Utilities.showAlert(getContext(), "Please check internet!", false);

        }
    }


}
