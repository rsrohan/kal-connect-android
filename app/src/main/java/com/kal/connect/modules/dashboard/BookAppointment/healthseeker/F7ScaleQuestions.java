package com.kal.connect.modules.dashboard.BookAppointment.healthseeker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.healthseeker.PastIssuesModel;
import com.kal.connect.models.healthseeker.PatientMealHabitsModel;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.complaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.inputParamsGET;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.oldComplaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.patientID;


public class F7ScaleQuestions extends Fragment {


    SeekBar[] seekBars = new SeekBar[7];
    int[] rating = new int[7];

    public F7ScaleQuestions() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_user_details7, container, false);

        seekBars[0] = v.findViewById(R.id.s1);
        seekBars[1] = v.findViewById(R.id.s2);
        seekBars[2] = v.findViewById(R.id.s3);
        seekBars[3] = v.findViewById(R.id.s4);
        seekBars[4] = v.findViewById(R.id.s5);
        seekBars[5] = v.findViewById(R.id.s6);
        seekBars[6] = v.findViewById(R.id.s7);

        setListener(seekBars[0], 0);
        setListener(seekBars[1], 1);
        setListener(seekBars[2], 2);
        setListener(seekBars[3], 3);
        setListener(seekBars[4], 4);
        setListener(seekBars[5], 5);
        setListener(seekBars[6], 6);

        if (oldComplaintID!=0){
            loadData();
        }
        return v;
    }

    private void setListener(SeekBar seekBar, int i) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    rating[i] = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void saveData(){
        HashMap<String, Object> inputParams = new HashMap<>();

        inputParams.put("PatientID", patientID);
        inputParams.put("ComplaintID", complaintID);
        inputParams.put("MealHabitRatingID", 0);
        inputParams.put("MealHabitIrregular", rating[0]);
        inputParams.put("SkipMealEasily", rating[1]);
        inputParams.put("ReadyToEatAnytime", rating[2]);
        inputParams.put("DisconfortOnHunger", rating[3]);
        inputParams.put("FullerBeforeMeal", rating[4]);
        inputParams.put("LittleMealUndigest", rating[5]);
        inputParams.put("UnevenDigestCycle", rating[6]);

        Log.e(F1Questions.TAG, "saveDataF7: "+inputParams.toString());

        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(F1Questions.TAG, response);

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
        String[] url = {Config.WEB_Services1, Config.F7_POST, "POST"};

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
                Log.e(F1Questions.TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("RespText") && !commonDataInfo.getString("RespText").isEmpty()) {

                                JSONObject respObj = new JSONObject(commonDataInfo.getString("RespText"));

                                seekBars[0].setProgress(Integer.parseInt(respObj.get("MealHabitIrregular").toString()));
                                seekBars[1].setProgress(Integer.parseInt(respObj.get("SkipMealEasily").toString()));
                                seekBars[2].setProgress(Integer.parseInt(respObj.get("ReadyToEatAnytime").toString()));
                                seekBars[3].setProgress(Integer.parseInt(respObj.get("DisconfortOnHunger").toString()));
                                seekBars[4].setProgress(Integer.parseInt(respObj.get("FullerBeforeMeal").toString()));
                                seekBars[5].setProgress(Integer.parseInt(respObj.get("LittleMealUndigest").toString()));
                                seekBars[6].setProgress(Integer.parseInt(respObj.get("UnevenDigestCycle").toString()));


                                Log.e(F1Questions.TAG, "responseCallback: " + respObj);

                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.F7_GET, "GET"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);

        }

    }
}