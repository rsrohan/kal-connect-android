package com.kal.connect.modules.dashboard.BookAppointment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
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

public class DoctorsListActivity extends CustomActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "ListOfDoctors";
    RecyclerView doctorsListRecyclerVw;
    DoctorsListAdapter doctorsListAdapter;
    ArrayList<DoctorModel> doctorslist = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);
        buildUI();
    }

    public void buildUI(){

        setHeaderView(R.id.headerView, DoctorsListActivity.this, DoctorsListActivity.this.getResources().getString(R.string.doctor_list));
        headerView.showBackOption();
        searchView = (SearchView) findViewById(R.id.search_txt_vw);
        //Turn iconified to false:
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        //To remove the keyboard, but make sure you keep the expanded version:
        searchView.clearFocus();
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
        //inputParams.put("InternationalVCCharge", AppPreferences.getInstance().getCountryCode());
        //inputParams.put("InternationalVCCharge", "+1");



        SoapAPIManager apiManager = new SoapAPIManager(DoctorsListActivity.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(TAG,response);

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
            Utilities.showAlert(DoctorsListActivity.this,"No Internet!",false);

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText.toLowerCase());

        return false;
    }
    void filter(String text){
        ArrayList<DoctorModel> temp = new ArrayList();
        for(DoctorModel d: doctorslist){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getName().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        doctorsListAdapter.updateList(temp);
    }
}
