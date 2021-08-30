package com.kal.connect.modules.dashboard.BookAppointment.healthseeker;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.healthseeker.F1Model;
import com.kal.connect.modules.dashboard.BuyMedicine.MedicineActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.complaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.inputParamsGET;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.oldComplaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.patientID;


public class F1Questions extends Fragment {


    EditText whom_u_live_with, hope_to_acheive, connect_with_yourself;
    public static String TAG = "HealthSeekerForm";

    public F1Questions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_user_details1, container, false);

        whom_u_live_with = v.findViewById(R.id.whom_u_live_with);
        hope_to_acheive = v.findViewById(R.id.hope_to_acheive);
        connect_with_yourself = v.findViewById(R.id.connect_with_yourself);

        if (oldComplaintID!=0){
            loadData();
        }

        return v;
    }

    public void setData(String s1, String s2, String s3){
        if (!s1.equals("null")){
            whom_u_live_with.setText(s1);
        }
        if (!s2.equals("null")) {
            hope_to_acheive.setText(s2);
        }
        if (!s3.equals("null")) {
            connect_with_yourself.setText(s3);
        }
    }

    public void loadData(){



        //Log.e(TAG, "loadDataF1: "+inputParams.toString());

        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParamsGET, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
               // Log.e(TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                            if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                                if (commonDataInfo.has("RespText") && !commonDataInfo.getString("RespText").isEmpty()) {
                                    JSONObject commonData = new JSONObject(commonDataInfo.get("RespText").toString());
                                    setData(
                                            commonData.getString("LiveWith"),
                                            commonData.getString("HealthConsultHope"),
                                            commonData.getString("SelfConnect")
                                    );
                                }
                            }
                    }
                } catch (Exception e) {
                   // Log.e(TAG, "responseCallback: "+e);

                }
            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.F1_QUESTIONS_GET, "GET"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);

        }

    }

    public void saveData(){
        String s1 = whom_u_live_with.getText().toString();
        String s2 = hope_to_acheive.getText().toString();
        String s3 = connect_with_yourself.getText().toString();

        if (!s1.isEmpty() || !s2.isEmpty() || !s3.isEmpty()) {

            //todo call POST api
            F1Model f1Model = new F1Model();
            f1Model.setLiveWith(s1);
            f1Model.setHealthConsultHope(s2);
            f1Model.setSelfConnect(s3);
            f1Model.setComplaintID(complaintID+"");
            f1Model.setPatientID(patientID);

            Gson gson = new Gson();
            String inputJson = gson.toJson(f1Model);

            HashMap<String, Object> inputParams = new Gson().fromJson(inputJson, HashMap.class);

            //Log.e(TAG, "loadDataF1: "+inputParams.toString());


            SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
                @Override
                public void responseCallback(Context context, String response) throws JSONException {
                  //  Log.e(TAG, response);

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
            String[] url = {Config.WEB_Services1, Config.F1_QUESTIONS_POST, "POST"};

            if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
                apiManager.execute(url);
            } else {
                Utilities.showAlert(getActivity(), "Please check internet!", false);

            }




        }else{
          //  Log.e(TAG, "saveDataF1: NO DATA FILLED");
        }

    }
}