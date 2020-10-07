package com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Examination;

import android.content.Context;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

//import com.aayurveda.doctor.R;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Examination.DataModel.GetPatientPojo;
import com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Examination.DataModel.MasterElement;
import com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Examination.DataModel.PatientExaminationData;
import com.kal.connect.modules.dashboard.tabs.Appointments.Tabs.Examination.DataModel.PatientModel;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;
//import com.medi360.doctor.doctorsapp.adapter.PatientExaminationAdapter;
//import com.medi360.doctor.doctorsapp.config.Configuration;
//import com.medi360.doctor.doctorsapp.pojo.GetPatientPojo;
//import com.medi360.doctor.doctorsapp.pojo.PatientModel;
//import com.medi360.doctor.doctorsapp.pojo.patientexamination.MasterElement;
//import com.medi360.doctor.doctorsapp.pojo.patientexamination.PatientExaminationData;
//import com.medi360.doctor.doctorsapp.werservice.AyurvedhaDataService;
//import com.medi360.doctor.doctorsapp.werservice.WebServiceLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class PatientExamination extends Fragment {

    RecyclerView mRvPatient;
    ArrayList<PatientModel> mAlPatient;
    private SectionedRecyclerViewAdapter sectionedAdapter;
//    PatientAdapter mPatientAdapter;

    PatientExaminationAdapter patientExaminationAdapter;
//    String json = Configuration.json;
    //    PatientAdapterTemp mAdapterTemp;
    ArrayList<MasterElement> masterElements = new ArrayList<>();
    ArrayList<GetPatientPojo> SelectedPatient = new ArrayList<>();
    Button mBtnUpdate;
    JSONObject jsonObjSend = new JSONObject();
    EditText mEdtExam;

    JSONObject appointmentDetails = GlobValues.getInstance(). getAppointmentCompleteDetails();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.patient_examination, container, false);

        sectionedAdapter = new SectionedRecyclerViewAdapter();
        mBtnUpdate = (Button) view.findViewById(R.id.btn_update);
        mEdtExam = (EditText) view.findViewById(R.id.edt_patient);
        try {
            getAllAyurvedaModules();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        getLocalJson();
        mRvPatient = (RecyclerView) view.findViewById(R.id.rv_patient_exam);

        mRvPatient.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvPatient.setHasFixedSize(true);

        mRvPatient.setVisibility(View.VISIBLE);

        return view;

    }

    void getPatientExamination(){
        try{
            if(appointmentDetails.getJSONArray("PatientExaminationDetails") != null ){
                JSONArray mJsonArray = appointmentDetails.getJSONArray("PatientExaminationDetails");
                for (int i = 0; i < mJsonArray.length(); i++) {
                    JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                    GetPatientPojo mGetPatientPojo = new GetPatientPojo();
                    mGetPatientPojo.setPatExaminationID(mJsonObject.getString("PatExaminationID"));
                    mGetPatientPojo.setPatientID(mJsonObject.getString("PatientID"));
                    mGetPatientPojo.setComplaintID(mJsonObject.getString("ComplaintID"));
                    mGetPatientPojo.setModuleID(mJsonObject.getString("ModuleID"));
                    mGetPatientPojo.setUpdatedDateTime(mJsonObject.getString("UpdatedDateTime"));
                    mGetPatientPojo.setSpecialistID(mJsonObject.getString("SpecialistID"));
                    mGetPatientPojo.setMasterElementTypeID(mJsonObject.getString("MasterElementTypeID"));
                    mGetPatientPojo.setSystematicExam(mJsonObject.getString("SystematicExam"));
                    if (mJsonObject.getString("SystematicExam") != null && !mJsonObject.getString("SystematicExam").equalsIgnoreCase("")) {
                        mEdtExam.setText(mJsonObject.getString("SystematicExam"));
                        mEdtExam.setEnabled(false);
                        mEdtExam.setClickable(false);
                        mEdtExam.setCursorVisible(false);
                    }
                    SelectedPatient.add(mGetPatientPojo);
                }
            }
        }catch (Exception e){

        }

        patientExaminationAdapter = new PatientExaminationAdapter(getActivity(), masterElements, mBtnUpdate, mEdtExam, SelectedPatient);
        sectionedAdapter.addSection(patientExaminationAdapter);
        mRvPatient.setAdapter(sectionedAdapter);

    }

    void getAllAyurvedaModules(){
        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);

                try{
                    Gson gson = new Gson();
                    PatientExaminationData patientExaminationData = gson.fromJson(response, PatientExaminationData.class);
                    masterElements = new ArrayList<>(patientExaminationData.getMasterElements());
                    getPatientExamination();
                }catch (Exception e){

                }


            }
        },true);
        String[] url = {Config.WEB_Services3,Config.GET_AYURVEDHA_MODULES,"GET"};

        if (Utilities.isNetworkAvailable(getContext())) {
            apiManager.execute(url);
        }else{

        }
    }


//    public class PatientExaminationTask extends AsyncTask<String, Void, String> {
//
//        public void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String responseString = null;
////            responseString = WebServiceLogin.WebserviceAuthenticateUser(params[0], "GetExaminationDetails");
//            responseString = AyurvedhaDataService.WebservicePrimitive(null, "GetAllAyurvedaModules");
////
//            return responseString;
//        }
//
//        @Override
//        public void onPostExecute(String responseString) {
//            if (responseString == null) {
//                return;
//            }
//            try {
//                if (responseString == null) {
//                    return;
//                }
//                Gson gson = new Gson();
//                PatientExaminationData patientExaminationData = gson.fromJson(responseString, PatientExaminationData.class);
//                masterElements = new ArrayList<>(patientExaminationData.getMasterElements());
//
//            } catch (Exception e) {
//                System.out.print("Usha exception in asynctask");
//            } finally {
//                try {
////                    jsonObjSend.put("ComplaintID", "1103");
//                    jsonObjSend.put("ComplaintID", Configuration.Selected_complaint_id);
//                    jsonObjSend.put("PatientID", Configuration.Selected_patient_id);
//                    new GetPatientExaminationTask().execute(jsonObjSend.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
////
//
//            }
//
//        }
//    }


//    public class GetPatientExaminationTask extends AsyncTask<String, Void, String> {
//
//        public void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String responseString = null;
//            responseString = WebServiceLogin.WebserviceAuthenticateUser(params[0], "GetExaminationDetails");
////            responseString = AyurvedhaDataService.WebservicePrimitive(null,"GetAllAyurvedaModules");
////
//            return responseString;
//        }
//
//        @Override
//        public void onPostExecute(String responseString) {
//            if (responseString == null) {
//                return;
//            }
//            try {
//                if (responseString == null) {
//                    return;
//                }
//                if (responseString != null && !responseString.equalsIgnoreCase("")) {
//                    JSONArray mJsonArray = new JSONArray(responseString);
//                    for (int i = 0; i < mJsonArray.length(); i++) {
//                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
//                        GetPatientPojo mGetPatientPojo = new GetPatientPojo();
//                        mGetPatientPojo.setPatExaminationID(mJsonObject.getString("PatExaminationID"));
//                        mGetPatientPojo.setPatientID(mJsonObject.getString("PatientID"));
//                        mGetPatientPojo.setComplaintID(mJsonObject.getString("ComplaintID"));
//                        mGetPatientPojo.setModuleID(mJsonObject.getString("ModuleID"));
//                        mGetPatientPojo.setUpdatedDateTime(mJsonObject.getString("UpdatedDateTime"));
//                        mGetPatientPojo.setSpecialistID(mJsonObject.getString("SpecialistID"));
//                        mGetPatientPojo.setMasterElementTypeID(mJsonObject.getString("MasterElementTypeID"));
//                        mGetPatientPojo.setSystematicExam(mJsonObject.getString("SystematicExam"));
//                        if (mJsonObject.getString("SystematicExam") != null && !mJsonObject.getString("SystematicExam").equalsIgnoreCase("")) {
//                            mEdtExam.setText(mJsonObject.getString("SystematicExam"));
//                            mEdtExam.setEnabled(false);
//                            mEdtExam.setClickable(false);
//                            mEdtExam.setCursorVisible(false);
//                        }
//                        SelectedPatient.add(mGetPatientPojo);
//                    }
//                }
//                patientExaminationAdapter = new PatientExaminationAdapter(getActivity(), masterElements, mBtnUpdate, mEdtExam, SelectedPatient);
//                sectionedAdapter.addSection(patientExaminationAdapter);
//            } catch (Exception e) {
//                System.out.print("Usha exception in asynctask");
//                patientExaminationAdapter = new PatientExaminationAdapter(getActivity(), masterElements, mBtnUpdate, mEdtExam, SelectedPatient);
//                sectionedAdapter.addSection(patientExaminationAdapter);
//            } finally {
//                mRvPatient.setAdapter(sectionedAdapter);
//            }
//
//        }
//    }

}
