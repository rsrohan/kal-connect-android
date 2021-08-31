package com.kal.connect.modules.dashboard.BookAppointment.healthseeker;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kal.connect.R;
import com.kal.connect.adapters.healthseeker.FamilyHisMasterAdapter;
import com.kal.connect.adapters.healthseeker.SymptomsMasterAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.IssueHeaderModel;
import com.kal.connect.models.IssuesModel;
import com.kal.connect.models.healthseeker.FamilyHistory;
import com.kal.connect.models.healthseeker.MasterData;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.complaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.inputParamsGET;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.oldComplaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.patientID;


public class F11FamilyHistory extends Fragment {

    public static final String TAG = "FamilyScreen";
    RecyclerView rv_problem_describe;
    TextView textHeader;
    private ArrayList<FamilyHistory> masterData;
    private FamilyHisMasterAdapter familyHisMasterAdapter;

    public F11FamilyHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_details10, container, false);
        rv_problem_describe = v.findViewById(R.id.problem_describe);
        textHeader = v.findViewById(R.id.textHeader);

        textHeader.setText("Family History");

        getIssuesList();

        return v;
    }




    void getIssuesList() {

        HashMap<String, Object> inputParams = new HashMap<String, Object>();


        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("RespText")) {

                            masterData = new ArrayList<>();
                            masterData = new Gson()
                                    .fromJson(commonDataInfo.get("RespText").toString(),
                                            new TypeToken<ArrayList<FamilyHistory>>() {
                                            }.getType());
                            //Log.e(TAG, "responseCallback: " + masterData.size());
                            familyHisMasterAdapter = new FamilyHisMasterAdapter(getContext(), masterData);
                            rv_problem_describe.setAdapter(familyHisMasterAdapter);
                            if (oldComplaintID!=0){
                                loadData();
                            }
                        }

                    }
                } catch (Exception e) {
                    Log.e(TAG, "responseCallback: " + e);

                }
            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.GET_FAMILY_HIS_DATA, "POST"};

        if (Utilities.isNetworkAvailable(getContext())) {
            apiManager.execute(url);
        }
    }

    public void saveData(){

        familyHisMasterAdapter.notifyDataSetChanged();

        ArrayList<HashMap<String, Object>> objArray = new ArrayList<>();

        for (int i = 0;i<masterData.size();i++){
            Gson gson = new Gson();
            String inputJson = gson.toJson(masterData.get(i));
            HashMap<String, Object> params = new Gson().fromJson(inputJson, HashMap.class);
            objArray.add(params);
        }
        HashMap<String, Object> inputParams = new HashMap<>();
        inputParams.put("PatientID", patientID);
        inputParams.put("ComplaintID", complaintID);
        inputParams.put("objArray", objArray);

        Log.e(TAG, "saveDataF11: "+inputParams.toString());

        if (masterData.size()>0){
            SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
                @Override
                public void responseCallback(Context context, String response) throws JSONException {
                    Log.e(TAG, response);

                    try {
                        JSONArray responseAry = new JSONArray(response);
                        if (responseAry.length() > 0) {
                            JSONObject commonDataInfo = responseAry.getJSONObject(0);
                            if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == -1) {
                                if (commonDataInfo.has("RespText") && !commonDataInfo.getString("RespText").isEmpty()) {
                                    Utilities.showAlert(getActivity(), commonDataInfo.getString("RespText"), false);
                                } else {
                                    Utilities.showAlert(getActivity(), "Something went wrong!", false);
                                }

                            }
                        }
                    } catch (Exception e) {
                        Utilities.showAlert(getActivity(), "Something went wrong!", false);
                    }
                }
            }, false);
            String[] url = {Config.WEB_Services1, Config.F11_POST, "POST"};

            if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
                apiManager.execute(url);
            } else {
                Utilities.showAlert(getActivity(), "Please check internet!", false);

            }
        }

    }
    public void loadData(){

//        inputParamsGET.put("PatientID", "42271");
//        inputParamsGET.put("ComplaintID", "4104");


        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParamsGET, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("RespText") && !commonDataInfo.getString("RespText").isEmpty()) {

                                Gson gson = new Gson();
                                Type listType = new TypeToken<ArrayList<FamilyHistory>>(){}.getType();
                                ArrayList<FamilyHistory> familyHistoryArrayList = gson.fromJson(commonDataInfo.get("RespText").toString(), listType);
                                for (int i=0;i<familyHistoryArrayList.size();i++){
                                    FamilyHistory fh = familyHistoryArrayList.get(i);
                                    masterData.get(i).setFamilyData(fh.isFather(),
                                            fh.isMother(),
                                            fh.isBrother(),
                                            fh.isSister(),
                                            fh.isPgm(),
                                            fh.isPgf(),
                                            fh.isMgm(),
                                            fh.isMgf());
                                }
                                //masterData.addAll(familyHistoryArrayList);
                                Log.e(TAG, "responseCallback: "+masterData);
                                familyHisMasterAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "responseCallback: "+e );

                }
            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.F11_GET, "GET"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);
        }

    }
}