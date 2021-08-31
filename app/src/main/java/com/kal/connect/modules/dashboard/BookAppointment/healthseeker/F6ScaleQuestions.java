package com.kal.connect.modules.dashboard.BookAppointment.healthseeker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kal.connect.R;
import com.kal.connect.adapters.healthseeker.DiseaseAdapter;
import com.kal.connect.adapters.healthseeker.MealAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.healthseeker.DiseaseModel;
import com.kal.connect.models.healthseeker.HospitalizationModel;
import com.kal.connect.models.healthseeker.PastIssuesModel;
import com.kal.connect.models.healthseeker.PatientMealHabitsModel;
import com.kal.connect.models.healthseeker.SurgeryModel;
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


public class F6ScaleQuestions extends Fragment {

    MealAdapter mealAdapter;

    RecyclerView mealsRecyclerview;

    SeekBar how_stressed, energy_level;

    int stressLevel = -1, energyLevel = -1;

    ArrayList<PatientMealHabitsModel> mealHabitsModels = new ArrayList<>();

    public F6ScaleQuestions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_details6, container, false);

        mealsRecyclerview = v.findViewById(R.id.rv);
        how_stressed = v.findViewById(R.id.how_stressed);
        energy_level = v.findViewById(R.id.energy_level);

        how_stressed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                stressLevel = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        energy_level.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                energyLevel = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Morning"));
        mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Mid-Morning"));
        mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Lunch"));
        mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Snack"));
        mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Evening"));
        mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Dinner"));
        mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Bedtime"));


        mealAdapter = new MealAdapter(getContext(), mealHabitsModels);
        mealsRecyclerview.setAdapter(mealAdapter);

        if (oldComplaintID!=0){
            loadData();
        }

        return v;
    }

    public void saveData() {

        mealAdapter.notifyDataSetChanged();

        ArrayList<HashMap<String, Object>> PatientMealHabits = new ArrayList<>();

        for (int i = 0; i < mealHabitsModels.size(); i++) {
            Gson gson = new Gson();
            String inputJson = gson.toJson(mealHabitsModels.get(i));
            HashMap<String, Object> params = new Gson().fromJson(inputJson, HashMap.class);
            PatientMealHabits.add(params);
        }

        HashMap<String, Object> inputParams = new HashMap<>();
        inputParams.put("PatientID", patientID);
        inputParams.put("ComplaintID", complaintID);
        inputParams.put("PatientMealHabits", PatientMealHabits);
        inputParams.put("StressRating", stressLevel);
        inputParams.put("EnergyRating", energyLevel);

        Log.e("F6ScaleQuestions", "saveDataF6: " + inputParams.toString());

        if (mealHabitsModels.size() > 0 || stressLevel >= 0 || energyLevel >= 0) {
            SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
                @Override
                public void responseCallback(Context context, String response) throws JSONException {
                    Log.e("F6ScaleQuestions", response);

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
            String[] url = {Config.WEB_Services1, Config.F6_POST, "POST"};

            if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
                apiManager.execute(url);
            } else {
                Utilities.showAlert(getActivity(), "Please check internet!", false);

            }
        }

    }

    public void loadData() {

        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParamsGET, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("F6ScaleQuestions", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("RespText") && !commonDataInfo.getString("RespText").isEmpty()) {

                                mealHabitsModels.clear();
                                JSONObject respObj = new JSONObject(commonDataInfo.getString("RespText"));
                                Gson gson = new Gson();
                                Type listType = new TypeToken<ArrayList<PatientMealHabitsModel>>() {
                                }.getType();
                                ArrayList<PatientMealHabitsModel> posts = gson.fromJson(respObj.get("PatientMealHabits").toString(), listType);
                                mealHabitsModels.addAll(posts);

                                if (mealHabitsModels.size()==0){
                                    mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Morning"));
                                    mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Mid-Morning"));
                                    mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Lunch"));
                                    mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Snack"));
                                    mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Evening"));
                                    mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Dinner"));
                                    mealHabitsModels.add(new PatientMealHabitsModel("0", "", "-1", "Bedtime"));

                                }
                                mealAdapter.notifyDataSetChanged();

                                int stress = respObj.getInt("StressRating");
                                int energy = respObj.getInt("EnergyRating");
                                how_stressed.setProgress(stress);
                                energy_level.setProgress(energy);


                                Log.e("F6ScaleQuestions", "responseCallback: " + posts.size());

                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("F6ScaleQuestions", "responseCallback: " +e);

                }
            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.F6_GET, "GET"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);

        }

    }


}