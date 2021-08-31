package com.kal.connect.modules.dashboard.BookAppointment.healthseeker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import lib.kingja.switchbutton.SwitchMultiButton;

import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.complaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.inputParamsGET;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.oldComplaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.patientID;


public class F9PersonalPreference extends Fragment {


    SwitchMultiButton[] switchMultiButtons = new SwitchMultiButton[5];
    int[] level = new int[5];

    public F9PersonalPreference() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_user_details9, container, false);

        switchMultiButtons[0] = v.findViewById(R.id.level1);
        switchMultiButtons[1] = v.findViewById(R.id.level2);
        switchMultiButtons[2] = v.findViewById(R.id.level3);
        switchMultiButtons[3] = v.findViewById(R.id.level4);
        switchMultiButtons[4] = v.findViewById(R.id.level5);

        setListener(switchMultiButtons[0], 0);
        setListener(switchMultiButtons[1], 1);
        setListener(switchMultiButtons[2], 2);
        setListener(switchMultiButtons[3], 3);
        setListener(switchMultiButtons[4], 4);

        if (oldComplaintID!=0){
            loadData();
        }
        return v;
    }

    private void setListener(SwitchMultiButton switchMultiButton, int i) {
        switchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                level[i] = position+1;
            }
        });
    }

    public void saveData(){
        HashMap<String, Object> inputParams = new HashMap<>();

        inputParams.put("PatientID", patientID);
        inputParams.put("ComplaintID", complaintID);
        inputParams.put("PreferredWeatherRating", level[0]);
        inputParams.put("UntoleratedWeatherRating", level[1]);
        inputParams.put("PreferredTasteRating", level[2]);
        inputParams.put("ThirstFeelRating", level[3]);
        inputParams.put("SweatRating", level[4]);


        Log.e("F9PersPref", "saveDataF9: "+inputParams.toString());

        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("F9PersPref", response);

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
        String[] url = {Config.WEB_Services1, Config.F9_POST, "POST"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);

        }
    }

    public void loadData() {


        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParamsGET, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("F9PersPref", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("RespText") && !commonDataInfo.getString("RespText").isEmpty()) {

                                JSONObject respObj = new JSONObject(commonDataInfo.getString("RespText"));

                                switchMultiButtons[0].setSelectedTab(Integer.parseInt(respObj.get("PreferredWeatherRating").toString())-1);
                                switchMultiButtons[1].setSelectedTab(Integer.parseInt(respObj.get("UntoleratedWeatherRating").toString())-1);
                                switchMultiButtons[2].setSelectedTab(Integer.parseInt(respObj.get("PreferredTasteRating").toString())-1);
                                switchMultiButtons[3].setSelectedTab(Integer.parseInt(respObj.get("ThirstFeelRating").toString())-1);
                                switchMultiButtons[4].setSelectedTab(Integer.parseInt(respObj.get("SweatRating").toString())-1);
/*[{"RespText":"{\"PreferredWeatherRating\":3,\"UntoleratedWeatherRating\":2,\"PreferredTasteRating\":5,\"ThirstFeelRating\":3,\"SweatRating\":3,\"PatientID\":42271,\"ComplaintID\":4076}","APIStatus":"1"}]*/
                                Log.e("F9PersPref", "responseCallback: " + respObj);

                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.F9_GET, "GET"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);

        }

    }
}