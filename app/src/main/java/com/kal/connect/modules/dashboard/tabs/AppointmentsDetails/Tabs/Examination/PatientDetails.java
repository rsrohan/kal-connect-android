package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

//import com.aayurveda.doctor.R;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel.AyurvedaModule;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel.GetPatientPojo;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel.MasterElement;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel.PatientExaminationData;
import com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel.SubElement;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class PatientDetails extends Fragment {

    RecyclerView mRvPatient;

    JSONObject jsonObjSend = new JSONObject();
    EditText mEdtIllness, mEdtTreatMent, mEdtFamilyHistory, mEdtDiet, mEdtBowel, mEdtAppetite, mEdtBladder, mEdtSleep, mEdtAddiction, mEdtExercise, mEdtMenstruval;
    ArrayList<GetPatientPojo> mAlSub = new ArrayList<>();
    private SectionedRecyclerViewAdapter sectionedAdapter;
    ArrayList<MasterElement> masterElements = new ArrayList<>();
    PatientGridViewAdapter mAdapter;

    JSONObject appointmentDetails = GlobValues.getInstance().getAppointmentCompleteDetails();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.patient_details, container, false);
        sectionedAdapter = new SectionedRecyclerViewAdapter();




        mRvPatient = (RecyclerView) view.findViewById(R.id.rv_patient);
        mRvPatient.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvPatient.setHasFixedSize(true);

        mRvPatient.setVisibility(View.VISIBLE);


        mEdtIllness = (EditText) view.findViewById(R.id.histor_of_illness);
        mEdtTreatMent = (EditText) view.findViewById(R.id.treatment_history);
        mEdtFamilyHistory = (EditText) view.findViewById(R.id.family_social_occupational);
        mEdtDiet = (EditText) view.findViewById(R.id.diet);
        mEdtBowel = (EditText) view.findViewById(R.id.bowel);
        mEdtAppetite = (EditText) view.findViewById(R.id.appetite);
        mEdtBladder = (EditText) view.findViewById(R.id.bladder);
        mEdtSleep = (EditText) view.findViewById(R.id.sleep);
        mEdtAddiction = (EditText) view.findViewById(R.id.addiction);
        mEdtExercise = (EditText) view.findViewById(R.id.excercise);
        mEdtMenstruval = (EditText) view.findViewById(R.id.menstrual);
        getAllAyurvedaModules();


        return view;
    }

    void updatePatientDetails() {
        try {
            if (appointmentDetails != null && appointmentDetails.getJSONArray("PatientHxDetails") != null) {
                JSONArray mJsonArray = appointmentDetails.getJSONArray("PatientHxDetails");
//                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                JSONObject mJsonObject = mJsonArray.getJSONArray(0).getJSONObject(0);
                if (mJsonObject != null) {
                    mEdtIllness.setText(mJsonObject.getString("HxOfPresentIllness"));
                    mEdtTreatMent.setText(mJsonObject.getString("TreatmentHx"));
                    mEdtFamilyHistory.setText(mJsonObject.getString("FamilysocialHx"));
                    mEdtDiet.setText(mJsonObject.getString("Diet"));
                    mEdtBowel.setText(mJsonObject.getString("Bowel"));
                    mEdtAppetite.setText(mJsonObject.getString("Appetite"));
                    mEdtBladder.setText(mJsonObject.getString("Bladder"));
                    mEdtSleep.setText(mJsonObject.getString("Sleep"));
                    mEdtAddiction.setText(mJsonObject.getString("Addiction"));
                    mEdtExercise.setText(mJsonObject.getString("Exercise"));
                    mEdtMenstruval.setText(mJsonObject.getString("Menstrual"));

                    JSONArray jsonArray = mJsonArray.getJSONArray(1);
                    for (int k = 0; k < jsonArray.length(); k++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(k);
                        GetPatientPojo mGetPatientPojo = new GetPatientPojo();
                        mGetPatientPojo.setPatExaminationID(jsonObject.getString("PatExaminationID"));
                        mGetPatientPojo.setPatientID(jsonObject.getString("PatientID"));
                        mGetPatientPojo.setComplaintID(jsonObject.getString("ComplaintID"));
                        mGetPatientPojo.setModuleID(jsonObject.getString("ModuleID"));
                        mGetPatientPojo.setSpecialistID(jsonObject.getString("SpecialistID"));
                        mGetPatientPojo.setMasterElementTypeID(jsonObject.getString("MasterElementTypeID"));
                        mAlSub.add(mGetPatientPojo);
                    }

                }

            }
            for (int k = 0; k < masterElements.size(); k++) {
                if (k == 0) {
                    MasterElement masterElement = masterElements.get(0);
                    SubElement subElement = masterElement.getSubElements().get(k);
                    for (int j = 0; j < subElement.getAyurvedaModules().size(); j++) {
                        AyurvedaModule mAyurvedaModule = subElement.getAyurvedaModules().get(j);
                        for (int i = 0; i < mAlSub.size(); i++) {
                            if (mAlSub.get(i) != null) {
                                GetPatientPojo mGetPatientPojo = mAlSub.get(i);
                                if (mGetPatientPojo.getModuleID() != null && mAyurvedaModule.getModuleID() != null && mAyurvedaModule.getModuleID() == Integer.parseInt(mGetPatientPojo.getModuleID().trim())) {
                                    AyurvedaModule DuplicateAyur = new AyurvedaModule();
                                    DuplicateAyur.setCheck(true);
                                    DuplicateAyur.setModule(mAyurvedaModule.getModule());
                                    subElement.getAyurvedaModules().set(j, DuplicateAyur);
                                }
                            }
                        }
                    }
                    mAdapter = new PatientGridViewAdapter(getActivity(), subElement.getAyurvedaModules());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                    mRvPatient.setLayoutManager(gridLayoutManager);
                }

            }
        } catch (Exception e) {

        } finally {
            mRvPatient.setAdapter(mAdapter);
        }
    }

    void getAllAyurvedaModules() {
        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);
                try {
                    Gson gson = new Gson();
                    PatientExaminationData patientExaminationData = gson.fromJson(response, PatientExaminationData.class);
                    masterElements = new ArrayList<>(patientExaminationData.getMasterElements());
                    updatePatientDetails();
                } catch (Exception e) {
                }
            }
        }, true);
        String[] url = {Config.WEB_Services3, Config.GET_AYURVEDHA_MODULES, "GET"};

        if (Utilities.isNetworkAvailable(getContext())) {
            apiManager.execute(url);
        } else {

        }
    }
}
