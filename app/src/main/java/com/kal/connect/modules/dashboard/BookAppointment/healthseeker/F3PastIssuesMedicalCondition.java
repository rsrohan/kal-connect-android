package com.kal.connect.modules.dashboard.BookAppointment.healthseeker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.kal.connect.R;
import com.kal.connect.adapters.healthseeker.DescribePastIssueAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.healthseeker.PastIssuesModel;
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
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.pastIssues;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.patientID;


public class F3PastIssuesMedicalCondition extends Fragment {

    RecyclerView rv_problem_describe;
    DescribePastIssueAdapter describeIssueAdapter;
    TextView add;
    private ArrayList<PastIssuesModel> issues;

    public F3PastIssuesMedicalCondition() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_details3, container, false);
        rv_problem_describe = v.findViewById(R.id.problem_describe);
        add = v.findViewById(R.id.add);

        issues = new ArrayList<>();
        for (int i = 0; i < pastIssues.size(); i++) {
            PastIssuesModel p = new PastIssuesModel();
            p.setCondition(pastIssues.get(i));
            issues.add(p);
        }
        describeIssueAdapter = new DescribePastIssueAdapter(getContext(), issues);
        rv_problem_describe.setAdapter(describeIssueAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PastIssuesModel p = new PastIssuesModel("0", "", "", "", "", "", "", "");
                if (issues.size() == 0 || !issues.get(0).getCondition().equals("")) {
                    issues.add(0, p);
                    describeIssueAdapter.notifyDataSetChanged();
                } else {
                    Utilities.showAlert(getContext(), "Fill current fields first", false);
                }
            }
        });
        if (oldComplaintID!=0){
            loadData();
        }
        return v;
    }

    public void saveData(){

        describeIssueAdapter.notifyDataSetChanged();

        ArrayList<HashMap<String, Object>> objArray = new ArrayList<>();

        for (int i = 0;i<issues.size();i++){
            Gson gson = new Gson();
            String inputJson = gson.toJson(issues.get(i));
            HashMap<String, Object> params = new Gson().fromJson(inputJson, HashMap.class);
            objArray.add(params);
        }
        HashMap<String, Object> inputParams = new HashMap<>();
        inputParams.put("PatientID", patientID);
        inputParams.put("ComplaintID", complaintID);
        inputParams.put("objArray", objArray);

        Log.e(F1Questions.TAG, "saveDataF3: "+inputParams.toString());

        if (issues.size()>0){
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
            String[] url = {Config.WEB_Services1, Config.F3_POST, "POST"};

            if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
                apiManager.execute(url);
            } else {
                Utilities.showAlert(getActivity(), "Please check internet!", false);

            }
        }

    }
    public void loadData(){


        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParamsGET, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(F1Questions.TAG, "loadDataF3: "+response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("RespText") && !commonDataInfo.getString("RespText").isEmpty()) {

//                                JSONArray jsonArray = new JSONArray(commonDataInfo.getString("RespText"));
//                                JSONObject respObj = jsonArray.getJSONObject(0);
                                Gson gson = new Gson();
                                Type listType = new TypeToken<ArrayList<PastIssuesModel>>(){}.getType();
                                ArrayList<PastIssuesModel> posts = gson.fromJson(commonDataInfo.get("RespText").toString(), listType);
                                Log.e(F1Questions.TAG, "responseCallback: "+posts.size());
                                issues.addAll(posts);
                                describeIssueAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Exception e) {
                    //Log.e("loadDataF3", "loadDataF3: "+e );

                }
            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.F3_GET, "GET"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);

        }

    }
}