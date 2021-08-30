package com.kal.connect.modules.dashboard.BookAppointment.healthseeker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kal.connect.R;
import com.kal.connect.adapters.healthseeker.AboutMedicationAdapter;
import com.kal.connect.adapters.healthseeker.DiseaseAdapter;
import com.kal.connect.adapters.healthseeker.HospitalizationAdapter;
import com.kal.connect.adapters.healthseeker.SurgeryAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.healthseeker.AboutMedicationModel;
import com.kal.connect.models.healthseeker.AboutSupplimentModel;
import com.kal.connect.models.healthseeker.DiseaseModel;
import com.kal.connect.models.healthseeker.DiseaseSurgeryHospitalizationModel;
import com.kal.connect.models.healthseeker.HospitalizationModel;
import com.kal.connect.models.healthseeker.PastIssuesModel;
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


public class F5PastDiseaseSurgeryHospitalization extends Fragment {

    RecyclerView any_disease, any_surgery, any_hospitalization;
    TextView addDisease, addSurgery, addHospitalization;
    DiseaseAdapter diseaseAdapter;
    SurgeryAdapter surgeryAdapter;
    HospitalizationAdapter hospitalizationAdapter;

    public F5PastDiseaseSurgeryHospitalization() {
        // Required empty public constructor
    }


    ArrayList<DiseaseModel> diseaseList = new ArrayList<>();
    ArrayList<SurgeryModel> surgeryList = new ArrayList<>();
    ArrayList<HospitalizationModel> hospList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_user_details5, container, false);

        any_disease = v.findViewById(R.id.any_disease);
        any_surgery = v.findViewById(R.id.any_surgery);
        any_hospitalization = v.findViewById(R.id.any_hospitalization);
        addDisease = v.findViewById(R.id.addDisease);
        addSurgery = v.findViewById(R.id.addInfo);
        addHospitalization = v.findViewById(R.id.addInfo2);


        diseaseAdapter = new DiseaseAdapter(getContext(), diseaseList);
        any_disease.setAdapter(diseaseAdapter);

        surgeryAdapter = new SurgeryAdapter(getContext(), surgeryList);
        any_surgery.setAdapter(surgeryAdapter);

        hospitalizationAdapter = new HospitalizationAdapter(getContext(), hospList);
        any_hospitalization.setAdapter(hospitalizationAdapter);


        setAddButton(addDisease, 1);
        setAddButton(addSurgery, 2);
        setAddButton(addHospitalization, 3);

        if (oldComplaintID!=0){
            loadData();
        }
        return v;
    }

    private void setAddButton(TextView add,  int type) {

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type==1){
                    DiseaseModel p = new DiseaseModel("0", "", "", "");
                    if (diseaseList.size() == 0 || !diseaseList.get(0).getDisease().equals("")) {
                        diseaseList.add(0, p);
                        diseaseAdapter.notifyDataSetChanged();


                    } else {
                        Utilities.showAlert(getContext(), "Fill current fields first", false);
                    }
                }

                if (type==2){
                    SurgeryModel p = new SurgeryModel("0", "", "", "", "");
                    if (surgeryList.size() == 0 || !surgeryList.get(0).getMethod().equals("")) {
                        surgeryList.add(0, p);
                        surgeryAdapter.notifyDataSetChanged();

                    } else {
                        Utilities.showAlert(getContext(), "Fill current fields first", false);
                    }
                }

                if (type==3){
                    HospitalizationModel p = new HospitalizationModel("0", "", "", "");
                    if (hospList.size() == 0 || !hospList.get(0).getYear().equals("")) {
                        hospList.add(0, p);
                        hospitalizationAdapter.notifyDataSetChanged();

                    } else {
                        Utilities.showAlert(getContext(), "Fill current fields first", false);
                    }
                }

            }
        });
    }
    public void saveData(){
        diseaseAdapter.notifyDataSetChanged();
        surgeryAdapter.notifyDataSetChanged();
        hospitalizationAdapter.notifyDataSetChanged();

        ArrayList<HashMap<String, Object>> PatientPastDiseases = new ArrayList<>();

        for (int i = 0;i<diseaseList.size();i++){
            Gson gson = new Gson();
            String inputJson = gson.toJson(diseaseList.get(i));
            HashMap<String, Object> params = new Gson().fromJson(inputJson, HashMap.class);
            PatientPastDiseases.add(params);
        }

        ArrayList<HashMap<String, Object>> PatientPastSurgeries = new ArrayList<>();

        for (int i = 0;i<surgeryList.size();i++){
            Gson gson = new Gson();
            String inputJson = gson.toJson(surgeryList.get(i));
            HashMap<String, Object> params = new Gson().fromJson(inputJson, HashMap.class);
            PatientPastSurgeries.add(params);
        }


        ArrayList<HashMap<String, Object>> PatientHospitalizations = new ArrayList<>();

        for (int i = 0;i<hospList.size();i++){
            Gson gson = new Gson();
            String inputJson = gson.toJson(hospList.get(i));
            HashMap<String, Object> params = new Gson().fromJson(inputJson, HashMap.class);
            PatientHospitalizations.add(params);
        }


        HashMap<String, Object> inputParams = new HashMap<>();
        inputParams.put("PatientID", patientID);
        inputParams.put("ComplaintID", complaintID);
        inputParams.put("PatientPastDiseases", PatientPastDiseases);
        inputParams.put("PatientPastSurgeries", PatientPastSurgeries);
        inputParams.put("PatientHospitalizations", PatientHospitalizations);

        Log.e(F1Questions.TAG, "saveDataF5: "+inputParams.toString());
        if (diseaseList.size()>0 || surgeryList.size()>0 || hospList.size()>0){
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
            String[] url = {Config.WEB_Services1, Config.F5_POST, "POST"};

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
                Log.e(F1Questions.TAG, response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject commonDataInfo = responseAry.getJSONObject(0);
                        if (commonDataInfo.has("APIStatus") && Integer.parseInt(commonDataInfo.getString("APIStatus")) == 1) {
                            if (commonDataInfo.has("RespText") && !commonDataInfo.getString("RespText").isEmpty()) {

                                diseaseList.clear();
                                surgeryList.clear();
                                hospList.clear();

                                JSONObject respObj = new JSONObject(commonDataInfo.getString("RespText"));
                                Gson gson = new Gson();
                                Type listType = new TypeToken<ArrayList<DiseaseModel>>(){}.getType();
                                ArrayList<DiseaseModel> posts = gson.fromJson(respObj.get("PatientPastDiseases").toString(), listType);
                                diseaseList.addAll(posts);
                                Type listType2 = new TypeToken<ArrayList<SurgeryModel>>(){}.getType();
                                ArrayList<SurgeryModel> posts2 = gson.fromJson(respObj.get("PatientPastSurgeries").toString(), listType2);
                                surgeryList.addAll(posts2);
                                Type listType3 = new TypeToken<ArrayList<HospitalizationModel>>(){}.getType();
                                ArrayList<HospitalizationModel> posts3 = gson.fromJson(respObj.get("PatientHospitalizations").toString(), listType3);
                                hospList.addAll(posts3);

                                diseaseAdapter.notifyDataSetChanged();
                                surgeryAdapter.notifyDataSetChanged();
                                hospitalizationAdapter.notifyDataSetChanged();



                                Log.e(F1Questions.TAG, "responseCallback: "+posts.size());

                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.F5_GET, "GET"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);

        }

    }
}