package com.kal.connect.modules.dashboard.BookAppointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
import com.kal.connect.models.IssuesModel;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination.DataModel.AyurvedaModule;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination.DataModel.GetPatientPojo;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination.DataModel.MasterElement;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination.DataModel.PatientExaminationData;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination.DataModel.SubElement;
import com.kal.connect.modules.dashboard.AppointmentsDetails.Tabs.Examination.PatientGridViewAdapter;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class PatientHistoryActivity extends CustomActivity {
    private ArrayList<IssuesModel> selectedIssuesModelList;

    RecyclerView mRvPatient;


    JSONObject jsonObjSend = new JSONObject();
    EditText mEdtIllness, mEdtTreatMent, mEdtFamilyHistory, mEdtDiet, mEdtBowel, mEdtAppetite, mEdtBladder, mEdtSleep, mEdtAddiction, mEdtExercise, mEdtMenstruval, historyOfIllness;
    ArrayList<GetPatientPojo> mAlSub = new ArrayList<>();
    private SectionedRecyclerViewAdapter sectionedAdapter;
    ArrayList<MasterElement> masterElements = new ArrayList<>();
    PatientGridViewAdapter mAdapter;

    JSONObject appointmentDetails = GlobValues.getInstance(). getAppointmentCompleteDetails();

    public ArrayList<String> selectedIDs = new ArrayList();





    @OnClick(R.id.next_btn)
    void proceed(){
        Intent intent = new Intent(PatientHistoryActivity.this, IssueDescriptorMapActivity.class);
        intent.putExtra("SelectedIssues", selectedIssuesModelList);

//        {
//            "PatientID": "",
//                "ComplaintID": "",
//                "HxOfPresentIllness": "",
//                "TreatmentHx": "",
//                "FamilysocialHx": "",
//                "Diet": "",
//                "Bowel": "",
//                "Appetite": "",
//                "Bladder": "",
//                "Sleep": "",
//                "Addiction": "",
//                "Menstrual": "",
//                "HxofPastIllness": ""
//        }

//        GlobValues.getInstance().addAppointmentInputParams("TreatmentHx", mEdtTreatMent.getText().toString());

         HashMap<String,Object> historyData = new HashMap<>();
//        historyData.put("", )

        historyData.put("TreatmentHx", mEdtTreatMent.getText().toString());
        historyData.put("FamilysocialHx", mEdtFamilyHistory.getText().toString());
        historyData.put("Diet", mEdtDiet.getText().toString());
        historyData.put("Bowel", mEdtBowel.getText().toString());
        historyData.put("Bladder", mEdtBladder.getText().toString());
        historyData.put("Sleep", mEdtSleep.getText().toString());
        historyData.put("Addiction", mEdtAddiction.getText().toString());
        historyData.put("Menstrual", mEdtMenstruval.getText().toString());
        historyData.put("HxOfPresentIllness", historyOfIllness.getText().toString());
        historyData.put("appetite", mEdtAppetite.getText().toString());
        historyData.put("Excerise", mEdtExercise.getText().toString());
        historyData.put("Excercise", mEdtExercise.getText().toString());
        historyData.put("HxofPastIllness", selectedIDs);

        GlobValues.getInstance().addAppointmentInputParams("objPatHx", historyData);





        startActivity(intent);
        Utilities.pushAnimation(PatientHistoryActivity.this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);
        selectedIssuesModelList =  (ArrayList<IssuesModel>)getIntent().getSerializableExtra("SelectedIssues");
        buildUI();
    }

    public void buildUI() {
        TextView tv_proceed = findViewById(R.id.tv_proceed);
        tv_proceed.setText(tv_proceed.getText()+" (2/3)");
        ButterKnife.bind(this);
        setHeaderView(R.id.headerView, PatientHistoryActivity.this, PatientHistoryActivity.this.getResources().getString(R.string.issue_descriptor_title));
        headerView.showBackOption();

        sectionedAdapter = new SectionedRecyclerViewAdapter();

        mRvPatient = (RecyclerView) findViewById(R.id.rv_patient);
        mRvPatient.setLayoutManager(new LinearLayoutManager(this));
        mRvPatient.setHasFixedSize(true);

        mRvPatient.setVisibility(View.VISIBLE);


        mEdtIllness = (EditText) findViewById(R.id.histor_of_illness);
        mEdtTreatMent = (EditText) findViewById(R.id.treatment_history);
        mEdtFamilyHistory = (EditText) findViewById(R.id.family_social_occupational);
        mEdtDiet = (EditText) findViewById(R.id.diet);
        mEdtBowel = (EditText) findViewById(R.id.bowel);
        mEdtAppetite = (EditText) findViewById(R.id.appetite);
        mEdtBladder = (EditText) findViewById(R.id.bladder);
        mEdtSleep = (EditText) findViewById(R.id.sleep);
        mEdtAddiction = (EditText) findViewById(R.id.addiction);
        mEdtExercise = (EditText) findViewById(R.id.excercise);
        mEdtMenstruval = (EditText) findViewById(R.id.menstrual);
        historyOfIllness = (EditText) findViewById(R.id.history_of_illness);
        getAllAyurvedaModules();
    }

    void getAllAyurvedaModules(){
        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        SoapAPIManager apiManager = new SoapAPIManager(this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***",response);
                try{
                    Gson gson = new Gson();
                    PatientExaminationData patientExaminationData = gson.fromJson(response, PatientExaminationData.class);
                    masterElements = new ArrayList<>(patientExaminationData.getMasterElements());
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
                            mAdapter = new PatientGridViewAdapter(PatientHistoryActivity.this, subElement.getAyurvedaModules());
                            mAdapter.setOnItemCheck(new PatientGridViewAdapter.OnItemCheck() {
                                @Override
                                public void onItemCheck(boolean check, String checkID) {
                                    if(check){
                                        selectedIDs.add(checkID);
                                    }else{
                                        selectedIDs.remove(checkID);
                                    }

                                }
                            });
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(PatientHistoryActivity.this, 3);
                            mRvPatient.setLayoutManager(gridLayoutManager);
                        }
                    }
                }catch (Exception e){
                }finally {
                    mRvPatient.setAdapter(mAdapter);
                }
            }
        },true);
        String[] url = {Config.WEB_Services3,Config.GET_AYURVEDHA_MODULES,"GET"};

        if (Utilities.isNetworkAvailable(this)) {
            apiManager.execute(url);
        }else{
        }
    }
}
