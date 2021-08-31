package com.kal.connect.modules.dashboard.BookAppointment.healthseeker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kal.connect.R;
import com.kal.connect.adapters.healthseeker.DescribePresentIssueAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.healthseeker.DescribeIssue;
import com.kal.connect.models.healthseeker.F1Model;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.complaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.inputParamsGET;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.issuesModels;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.oldComplaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.patientID;


public class F2DescribeIssue extends Fragment {

    RecyclerView rv_problem_describe;
    DescribePresentIssueAdapter describeIssueAdapter;
    ArrayList<DescribeIssue> describeIssueArrayList = new ArrayList<>();


    public F2DescribeIssue() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_details2, container, false);

        rv_problem_describe = v.findViewById(R.id.problem_describe);

        for (int i = 0; i < issuesModels.size(); i++) {

            DescribeIssue describeIssue = new DescribeIssue();
            describeIssue.setProblemName(issuesModels.get(i).getTitle());
            describeIssue.setMainProblemID(issuesModels.get(i).getId());
            describeIssueArrayList.add(describeIssue);

        }


        describeIssueAdapter = new DescribePresentIssueAdapter(getContext(), describeIssueArrayList);

        rv_problem_describe.setAdapter(describeIssueAdapter);


        return v;
    }

    public void saveData(){
        describeIssueAdapter.notifyDataSetChanged();

        ArrayList<HashMap<String, Object>> objArray = new ArrayList<>();

        for (int i = 0;i<describeIssueArrayList.size();i++){
            Gson gson = new Gson();
            String inputJson = gson.toJson(describeIssueArrayList.get(i));
            HashMap<String, Object> params = new Gson().fromJson(inputJson, HashMap.class);
            objArray.add(params);
        }
        HashMap<String, Object> inputParams = new HashMap<>();
        inputParams.put("PatientID", patientID);
        inputParams.put("ComplaintID", complaintID);
        inputParams.put("objArray", objArray);

       // Log.e(F1Questions.TAG, "saveDataF2: "+inputParams.toString());
        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e(F1Questions.TAG, "describe issue: "+response);

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
        String[] url = {Config.WEB_Services1, Config.F2_DESCRIBE_ISSUE_POST, "POST"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);

        }

    }

    public void loadData(){


        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParamsGET, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
               // Log.e(F1Questions.TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("RespText") && !commonDataInfo.getString("RespText").isEmpty()) {
                                //setData(commonDataInfo.getString("LiveWith"), commonDataInfo.getString("HealthConsultHope"), commonDataInfo.getString("SelfConnect"));
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.F2_DESCRIBE_ISSUE_GET, "GET"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);

        }

    }

}