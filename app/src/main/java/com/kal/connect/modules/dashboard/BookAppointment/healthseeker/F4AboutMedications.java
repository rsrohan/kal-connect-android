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
import com.google.gson.reflect.TypeToken;
import com.kal.connect.R;
import com.kal.connect.adapters.healthseeker.AboutMedicationAdapter;
import com.kal.connect.adapters.healthseeker.AboutSupplimentAdapter;
import com.kal.connect.adapters.healthseeker.DescribePastIssueAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.healthseeker.AboutMedicationModel;
import com.kal.connect.models.healthseeker.AboutSupplimentModel;
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
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.patientID;


public class F4AboutMedications extends Fragment {

    RecyclerView medi_describe, medi_describe2, medi_describe3;
    AboutMedicationAdapter aboutMedicationAdapter, aboutMedicineAdapter;
    AboutSupplimentAdapter aboutSupplementAdapter;
    TextView addMedication, addMedicine, addSupplement;
    ArrayList<AboutMedicationModel> medicationList = new ArrayList<>();
    ArrayList<AboutMedicationModel> medicineList = new ArrayList<>();
    ArrayList<AboutSupplimentModel> supplementList = new ArrayList<>();

    public F4AboutMedications() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_details4, container, false);

        medi_describe = v.findViewById(R.id.medi_describe);
        medi_describe2 = v.findViewById(R.id.medi_describe2);
        medi_describe3 = v.findViewById(R.id.medi_describe3);
        addMedication = v.findViewById(R.id.addMedication);
        addMedicine = v.findViewById(R.id.addMedicine);
        addSupplement = v.findViewById(R.id.addSupplement);


        aboutMedicationAdapter = new AboutMedicationAdapter(getContext(), medicationList);
        medi_describe.setAdapter(aboutMedicationAdapter);

        aboutMedicineAdapter = new AboutMedicationAdapter(getContext(), medicineList);
        medi_describe2.setAdapter(aboutMedicineAdapter);

        aboutSupplementAdapter = new AboutSupplimentAdapter(getContext(), supplementList);
        medi_describe3.setAdapter(aboutSupplementAdapter);


        setAddButton(addMedication, aboutMedicationAdapter, medicationList);
        setAddButton(addMedicine, aboutMedicineAdapter, medicineList);
        setAddButton2(addSupplement, aboutSupplementAdapter, supplementList);


        if (oldComplaintID!=0){
            loadData();
        }
        return v;
    }

    private void setAddButton(TextView add, AboutMedicationAdapter adapter, ArrayList<AboutMedicationModel> list) {

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutMedicationModel p = new AboutMedicationModel("0", "", "", "", "", "",true);
                if (list.size() == 0 || !list.get(0).getMedicineName().equals("")) {
                    list.add(0, p);
                    adapter.notifyDataSetChanged();
                } else {
                    Utilities.showAlert(getContext(), "Fill current fields first", false);
                }
            }
        });
    }

    private void setAddButton2(TextView add, AboutSupplimentAdapter adapter, ArrayList<AboutSupplimentModel> list) {

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutSupplimentModel p = new AboutSupplimentModel("0", "", "", "", "", "false", "");
                if (list.size() == 0 || !list.get(0).getName().equals("")) {
                    list.add(0, p);
                    adapter.notifyDataSetChanged();
                } else {
                    Utilities.showAlert(getContext(), "Fill current fields first", false);
                }
            }
        });
    }

    public void saveData() {
        aboutMedicationAdapter.notifyDataSetChanged();
        aboutMedicineAdapter.notifyDataSetChanged();
        aboutSupplementAdapter.notifyDataSetChanged();

        for (int i=0;i<medicationList.size();i++){
            medicationList.get(i).setAlternative(false);
        }
        for (int i=0;i<medicineList.size();i++){
            medicineList.get(i).setAlternative(true);
        }

        ArrayList<HashMap<String, Object>> PrescribedMedicines = new ArrayList<>();

        for (int i = 0;i<medicationList.size();i++){
            Gson gson = new Gson();
            String inputJson = gson.toJson(medicationList.get(i));
            HashMap<String, Object> params = new Gson().fromJson(inputJson, HashMap.class);
            PrescribedMedicines.add(params);
        }
        for (int i = 0;i<medicineList.size();i++){
            Gson gson = new Gson();
            String inputJson = gson.toJson(medicineList.get(i));
            HashMap<String, Object> params = new Gson().fromJson(inputJson, HashMap.class);
            PrescribedMedicines.add(params);
        }

        ArrayList<HashMap<String, Object>> PrescribedSuppliments = new ArrayList<>();

        for (int i = 0;i<supplementList.size();i++){
            Gson gson = new Gson();
            String inputJson = gson.toJson(supplementList.get(i));
            HashMap<String, Object> params = new Gson().fromJson(inputJson, HashMap.class);
            PrescribedSuppliments.add(params);
        }


        HashMap<String, Object> inputParams = new HashMap<>();
        inputParams.put("PatientID", patientID);
        inputParams.put("ComplaintID", complaintID);
        inputParams.put("PrescribedMedicines", PrescribedMedicines);
        inputParams.put("PrescribedSuppliments", PrescribedSuppliments);

       // Log.e(F1Questions.TAG, "saveDataF4: "+inputParams.toString());
        if (medicationList.size()>0 || medicineList.size()>0 || supplementList.size()>0){
            SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
                @Override
                public void responseCallback(Context context, String response) throws JSONException {
                   // Log.e(F1Questions.TAG, response);

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
            String[] url = {Config.WEB_Services1, Config.F4_POST, "POST"};

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

                                JSONObject respObj = new JSONObject(commonDataInfo.getString("RespText"));
                                Gson gson = new Gson();
                                Type listType = new TypeToken<ArrayList<AboutMedicationModel>>(){}.getType();
                                ArrayList<AboutMedicationModel> posts = gson.fromJson(respObj.get("PrescribedMedicines").toString(), listType);

                                medicineList.clear();
                                medicationList.clear();
                                supplementList.clear();
                                for (int i=0;i<posts.size();i++){
                                    if (posts.get(i).isAlternative()){
                                        medicineList.add(posts.get(i));
                                    }else{
                                        medicationList.add(posts.get(i));
                                    }
                                }
                                Type listType2 = new TypeToken<ArrayList<AboutSupplimentModel>>(){}.getType();
                                ArrayList<AboutSupplimentModel> posts2 = gson.fromJson(respObj.get("PrescribedSuppliments").toString(), listType2);

                                supplementList.addAll(posts2);

                                aboutMedicationAdapter.notifyDataSetChanged();
                                aboutMedicineAdapter.notifyDataSetChanged();
                                aboutSupplementAdapter.notifyDataSetChanged();

                               // Log.e(F1Questions.TAG, "responseCallback: "+posts.size());

                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.F4_GET, "GET"};

        if (Utilities.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            apiManager.execute(url);
        } else {
            Utilities.showAlert(getActivity(), "Please check internet!", false);

        }

    }
}