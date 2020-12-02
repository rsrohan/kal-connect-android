package com.kal.connect.modules.dashboard.tabs.HomeScreen;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kal.connect.R;
import com.kal.connect.adapters.DoctorsListAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.models.DoctorModel;
import com.kal.connect.models.HospitalModel;
import com.kal.connect.utilities.AppComponents;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class DoctorsListActivity extends CustomActivity {

    RecyclerView doctorsListRecyclerVw;
    DoctorsListAdapter doctorsListAdapter;
    ArrayList<DoctorModel> doctorslist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_list);
        buildUI();
    }

    public void buildUI(){

        setHeaderView(R.id.headerView, DoctorsListActivity.this, DoctorsListActivity.this.getResources().getString(R.string.doctor_list));
        headerView.showBackOption();

        doctorsListRecyclerVw = (RecyclerView) findViewById(R.id.doctors_list);
//        createDoctorsList();
        doctorsListAdapter = new DoctorsListAdapter(this,doctorslist);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(DoctorsListActivity.this, LinearLayoutManager.VERTICAL, false);
        doctorsListRecyclerVw.setLayoutManager(horizontalLayoutManager);
        doctorsListRecyclerVw.setAdapter(doctorsListAdapter);

        if (DoctorsListActivity.this != null)
            AppComponents.reloadCustomDataWithEmptyHint(doctorsListRecyclerVw, doctorsListAdapter, doctorslist, this.getResources().getString(R.string.no_doctors_found));

        getDoctorsList();

    }

    public void createDoctorsList(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type hospitalListType = new TypeToken<ArrayList<DoctorModel>>(){}.getType();
        doctorslist = gson.fromJson(response, hospitalListType);
        doctorsListAdapter = new DoctorsListAdapter(this,doctorslist);
        AppComponents.reloadCustomDataWithEmptyHint(doctorsListRecyclerVw, doctorsListAdapter, doctorslist, this.getResources().getString(R.string.no_doctors_found));
    }

    public void createDoctorsList(JSONArray doctorsListAry){
        for (int loop = 0; loop < doctorsListAry.length(); loop++) {

            try {
//                JSONObject singleObj = doctorsListAry.getJSONObject(loop);
//
//                DoctorModel d = new DoctorModel(singleObj.getString("SpecialistID"),singleObj.getString("SpecializationName"),
//                        singleObj.getString("Qualification"),"",singleObj.getString("Name"),"",
//                        (singleObj.has("DocCharge") && !singleObj.getString("DocCharge").isEmpty())?singleObj.getString("DocCharge"):"300",
//                        singleObj.getString("TechnicianCharge"),singleObj.getBoolean("isLoggedIn"));
//
//                if(!singleObj.getString("CityID").isEmpty())
//                {
//                    String cityLocation = GlobValues.cityNameFromID(singleObj.getString("CityID"));
//                    if(!cityLocation.isEmpty())
//                        d.setLocation(cityLocation);
//                }
//
//
//
//                String description = "";
//
//                if(singleObj.has("Breifnote") && !singleObj.getString("Breifnote").isEmpty()){
//                    description = singleObj.getString("Breifnote");
//                }
////                if(singleObj.has("DocLocation") && !singleObj.getString("DocLocation").isEmpty()){
////                    description = singleObj.getString("DocLocation");
////                }
//                d.setDescription(description);
//                doctorslist.add(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        if(doctorsListAry.length()==0){
//            EmptyRecyclerViewAdapter emptyAdapter = new EmptyRecyclerViewAdapter("No doctors found!");
//            doctorsListRecyclerVw.setAdapter(emptyAdapter);
//            emptyAdapter.notifyDataSetChanged();
//            if (DoctorsList.this != null)
//                AppComponents.reloadCustomDataWithEmptyHint(doctorsListRecyclerVw, doctorsListAdapter, doctorslist, this.getResources().getString(R.string.no_appointments_found));

//        }
//        doctorsListAdapter.notifyDataSetChanged();

        if (DoctorsListActivity.this != null)
            AppComponents.reloadCustomDataWithEmptyHint(doctorsListRecyclerVw, doctorsListAdapter, doctorslist, this.getResources().getString(R.string.no_doctors_found));

    }
    void getDoctorsList(){
        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();

        HashMap<String, Object> appointmentParms = GlobValues.getInstance().getAddAppointmentParams();


//        inputParams.put("AppointmentDate","2/15/2019");
//        inputParams.put("ConsultationMode","0");
//        inputParams.put("isInstant","0");


//        inputParams.put("AppointmentDate",appointmentParms.get("AppointmentDate").toString());
//        inputParams.put("AppTime",appointmentParms.get("AppointmentTime").toString());
//        inputParams.put("ConsultationMode","0");
//        inputParams.put("isInstant",appointmentParms.get("isInstant").toString());

        HospitalModel hospital = Utilities.selectedHospitalModel;
        inputParams.put("HospitalID",hospital.getHospitalID());
        inputParams.put("Lattitude",hospital.getHospitalLat());
        inputParams.put("Longitude",hospital.getHospitalLong());
        inputParams.put("CurrentTime",Utilities.getCurrentTime());



        SoapAPIManager apiManager = new SoapAPIManager(DoctorsListActivity.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    JSONArray responseAry = new JSONArray(response);
                    if(responseAry.length()>0){
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if(commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1){
                            if(commonDataInfo.has("APIStatus") && !commonDataInfo.getString("Message").isEmpty()){
                                Utilities.showAlert(DoctorsListActivity.this,commonDataInfo.getString("Message"),false);
                            }else{
                                Utilities.showAlert(DoctorsListActivity.this,"Please check again!",false);
                            }
                            return;

                        }
                        createDoctorsList(response);
//                        createDoctorsList(responseAry);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },true);
        String[] url = {Config.WEB_Services1,Config.GET_HOSPITAL_DOCTORS_LIST,"POST"};

        if (Utilities.isNetworkAvailable(DoctorsListActivity.this)) {
            apiManager.execute(url);
        }else{

        }
    }
}
