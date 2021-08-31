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


public class F8Habits extends Fragment {

    SeekBar[] seekBars = new SeekBar[6];
    ArrayList<HabitModel> habitModelArrayList = new ArrayList<>();
    EditText otherHabits;
    boolean isUpdated = false;

    public F8Habits() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_details8, container, false);
        seekBars[0] = v.findViewById(R.id.s1);
        seekBars[1] = v.findViewById(R.id.s2);
        seekBars[2] = v.findViewById(R.id.s3);
        seekBars[3] = v.findViewById(R.id.s4);
        seekBars[4] = v.findViewById(R.id.s5);
        seekBars[5] = v.findViewById(R.id.s6);
        otherHabits = v.findViewById(R.id.otherHabits);

        habitModelArrayList.add(new HabitModel("1", "1", "Alcohol", "-1"));
        habitModelArrayList.add(new HabitModel("2", "2", "Coffee", "-1"));
        habitModelArrayList.add(new HabitModel("3", "3", "Tea", "-1"));
        habitModelArrayList.add(new HabitModel("4", "4", "Tobacco", "-1"));
        habitModelArrayList.add(new HabitModel("5", "5", "Marijuana", "-1"));
        //habitModelArrayList.add(new HabitModel("", "", "Alcohol", ""));

        setListener(seekBars[0], 0);
        setListener(seekBars[1], 1);
        setListener(seekBars[2], 2);
        setListener(seekBars[3], 3);
        setListener(seekBars[4], 4);
        setListener(seekBars[5], 5);

        if (oldComplaintID!=0){
            loadData();
        }
        return v;
    }

    private void setListener(SeekBar seekBar, int i) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (i == 5 && habitModelArrayList.size() < 6) {
                    habitModelArrayList.add(new HabitModel("6", "6", "" + otherHabits.getText().toString(), "" + progress));
                } else {
                    habitModelArrayList.get(i).setHabitRating(progress + "");
                }
                isUpdated = true;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public class HabitModel {
        String PatientHabitID="0", HabitID, HabitRating, Comments;

        public HabitModel() {
        }

        public HabitModel(String patientHabitID, String habitID, String comments, String habitRating) {
            PatientHabitID = patientHabitID;
            HabitID = habitID;
            HabitRating = habitRating;
            Comments = comments;
        }

        public String getPatientHabitID() {
            return PatientHabitID;
        }

        public void setPatientHabitID(String patientHabitID) {
            PatientHabitID = patientHabitID;
        }

        public String getHabitID() {
            return HabitID;
        }

        public void setHabitID(String habitID) {
            HabitID = habitID;
        }

        public String getHabitRating() {
            return HabitRating;
        }

        public void setHabitRating(String habitRating) {
            HabitRating = habitRating;
        }

        public String getComments() {
            return Comments;
        }

        public void setComments(String comments) {
            Comments = comments;
        }
    }

    public void saveData() {
        ArrayList<HashMap<String, Object>> objArray = new ArrayList<>();

        for (int i = 0;i<habitModelArrayList.size();i++){
            Gson gson = new Gson();
            String inputJson = gson.toJson(habitModelArrayList.get(i));
            HashMap<String, Object> params = new Gson().fromJson(inputJson, HashMap.class);
            objArray.add(params);
        }
        HashMap<String, Object> inputParams = new HashMap<>();
        inputParams.put("PatientID", patientID);
        inputParams.put("ComplaintID", complaintID);
        inputParams.put("objArray", objArray);

        Log.e("F8Habits", "saveDataF8: "+inputParams.toString());
        if (isUpdated){
            SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
                @Override
                public void responseCallback(Context context, String response) throws JSONException {
                    Log.e("F8Habits", "saveDataF8"+response);

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
            String[] url = {Config.WEB_Services1, Config.F8_POST, "POST"};

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
                Log.e("F8Habits", "loadDataF8"+response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("RespText") && !commonDataInfo.getString("RespText").isEmpty()) {
                                //JSONObject respObj = new JSONObject(commonDataInfo.getString("RespText"));
                                Gson gson = new Gson();
                                Type listType = new TypeToken<ArrayList<HabitModel>>() {
                                }.getType();
                                ArrayList<HabitModel> posts = gson.fromJson(commonDataInfo.get("RespText").toString(), listType);
                                habitModelArrayList.addAll(posts);
                                for (int i=0;i<posts.size();i++){
                                    seekBars[i].setProgress(Integer.parseInt(posts.get(i).getHabitRating()));
                                }
                                Log.e("F8Habits", "responseCallback: " + commonDataInfo);

                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("F8Habits", "loadDataF8 responseCallback: "+e );

                }
            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.F8_GET, "GET"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);

        }

    }
}