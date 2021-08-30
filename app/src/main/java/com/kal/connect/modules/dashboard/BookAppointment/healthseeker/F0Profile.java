package com.kal.connect.modules.dashboard.BookAppointment.healthseeker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kal.connect.R;
import com.kal.connect.adapters.healthseeker.DemographyMasterAdapter;
import com.kal.connect.adapters.healthseeker.DemographySubElementAdapter;
import com.kal.connect.adapters.healthseeker.SymptomsMasterAdapter;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.models.healthseeker.DemographyData;
import com.kal.connect.models.healthseeker.F1Model;
import com.kal.connect.models.healthseeker.MasterData;
import com.kal.connect.modules.dashboard.AccountDetails.EditAccountActivity;
import com.kal.connect.modules.dashboard.DashboardMapActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.GlobValues;
import com.kal.connect.utilities.Utilities;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import lib.kingja.switchbutton.SwitchMultiButton;

import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.complaintID;
import static com.kal.connect.modules.dashboard.BookAppointment.healthseeker.HealthSeekerActivity.patientID;


public class F0Profile extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    public static String TAG = "HealthSeekerForm";
    EditText firName, lastName, mobile, weight, height, dob, city, state, addressLine1, addressLine2, pincode, email, password, occupation, emer_name, emer_phone, referred_by, pob;

    ArrayList<String> cityOptionsAry = new ArrayList<>();
    ArrayList<String> cityOptionsIdsAry = new ArrayList<>();

    ArrayList<String> stateOptionsAry = new ArrayList<>();
    ArrayList<String> stateOptionsIdsAry = new ArrayList<>();

    AlertDialog cityDialog, stateDialog;
    String selectedState;
    String selectedCity;
    Button proceedBtn;
    SwitchMultiButton genderOptions;

    DatePickerDialog datepickerdialog;

    private ArrayList<DemographyData> masterData;
    private DemographyMasterAdapter masterAdapter;
    RecyclerView demogRv;
    private String marital;
    private String ethnicity;

    public F0Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_details0, container, false);


        buildUI(v);
        return v;
    }

    private void buildUI(View v) {

        firName = (EditText) v.findViewById(R.id.fir_name);
        lastName = (EditText) v.findViewById(R.id.last_name);
        mobile = (EditText) v.findViewById(R.id.mobile);
        dob = (EditText) v.findViewById(R.id.dob);
        weight = (EditText) v.findViewById(R.id.txtWeight);
        height = (EditText) v.findViewById(R.id.height);
        city = (EditText) v.findViewById(R.id.city);
        state = (EditText) v.findViewById(R.id.state);
        addressLine1 = (EditText) v.findViewById(R.id.address_line1);
        addressLine2 = (EditText) v.findViewById(R.id.address_line2);
        pincode = (EditText) v.findViewById(R.id.pincode);
        email = (EditText) v.findViewById(R.id.email);
        password = (EditText) v.findViewById(R.id.password);
        occupation = (EditText) v.findViewById(R.id.occupation);
        emer_name = (EditText) v.findViewById(R.id.emergency_name);
        emer_phone = (EditText) v.findViewById(R.id.emergency_phone);
        referred_by = (EditText) v.findViewById(R.id.referred_by);
        pob = v.findViewById(R.id.pob);

        proceedBtn = (Button) v.findViewById(R.id.updatebutton);
        genderOptions = (SwitchMultiButton) v.findViewById(R.id.gender_options);


        Calendar now = Calendar.getInstance();
        datepickerdialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datepickerdialog.setThemeDark(true); //set dark them for dialog?
        datepickerdialog.vibrate(true); //vibrate on choosing date?
        datepickerdialog.dismissOnPause(true); //dismiss dialog when onPause() called?
        datepickerdialog.showYearPickerFirst(false); //choose year first?
        datepickerdialog.setAccentColor(Color.parseColor("#edb100")); // custom accent color
        datepickerdialog.setTitle(getResources().getString(R.string.select_dob)); //dialog title
        datepickerdialog.setMaxDate(now);


        dob.setOnClickListener(this);

        setStatesDialog();

        proceedBtn.setOnClickListener(this);

        demogRv = v.findViewById(R.id.demogRv);

        masterData = new ArrayList<>();

        DemographyData d1 = new DemographyData();
        d1.setDemographyType("Marital Status");
        ArrayList<DemographyData.objSubElements> d1_1 = new ArrayList<>();
        d1_1.add(new DemographyData.objSubElements("Married", false));
        d1_1.add(new DemographyData.objSubElements("Single", false));
        d1_1.add(new DemographyData.objSubElements("Divorced/Separated", false));
        d1_1.add(new DemographyData.objSubElements("Cohabitating", false));
        d1_1.add(new DemographyData.objSubElements("Widowed", false));
        d1.setObjSubElements(d1_1);

        DemographyData d2 = new DemographyData();
        d2.setDemographyType("What is your ethnicity?");
        ArrayList<DemographyData.objSubElements> d2_1 = new ArrayList<>();
        d2_1.add(new DemographyData.objSubElements("Native American", false));
        d2_1.add(new DemographyData.objSubElements("Asian", false));
        d2_1.add(new DemographyData.objSubElements("Hispanic", false));
        d2_1.add(new DemographyData.objSubElements("Mediterranean", false));
        d2_1.add(new DemographyData.objSubElements("African American", false));
        d2_1.add(new DemographyData.objSubElements("South Asian", false));
        d2_1.add(new DemographyData.objSubElements("Caucasian", false));
        d2_1.add(new DemographyData.objSubElements("Northern European", false));
        d2_1.add(new DemographyData.objSubElements("Other", false));
        d2.setObjSubElements(d2_1);

        masterData.add(d1);
        masterData.add(d2);
        masterAdapter = new DemographyMasterAdapter(getContext(), masterData);
        demogRv.setAdapter(masterAdapter);

        setupAccInfo();

    }

    void setupAccInfo() {
        try {
            JSONObject accInfo = AppPreferences.getInstance().getUserInfo();
            Log.e(TAG, "setupAccInfo: "+accInfo.toString() );
            firName.setText(accInfo.getString("FirstName"));
            lastName.setText(accInfo.getString("LastName"));
            mobile.setText(accInfo.getString("ContactNo"));
            String dobString = Utilities.dateRT(accInfo.getString("DOB"));
            //dobString = Utilities.changeStringFormat(dobString, "yyyy-mm-dd", "dd/mm/yyyy");

            dob.setText(dobString);
            if (!accInfo.getString("Sex").isEmpty()) {
                if (Integer.parseInt(accInfo.getString("Sex").trim()) == 1) {
                    genderOptions.setSelectedTab(0);
                } else if (Integer.parseInt(accInfo.getString("Sex").trim()) == 2) {
                    genderOptions.setSelectedTab(1);
                } else if (Integer.parseInt(accInfo.getString("Sex").trim()) == 2) {
                    genderOptions.setSelectedTab(0);
                }
            }

            genderOptions.setSelected(false);

            city.setOnClickListener(this);
            state.setOnClickListener(this);
            dob.setOnClickListener(this);

            if (accInfo.has("CityID") && !accInfo.getString("CityID").isEmpty()) {
                city.setTag(Integer.parseInt(accInfo.getString("CityID")));
                selectedCity = accInfo.getString("CityID");
                city.setText(accInfo.getString("cityname"));
            }

            if (accInfo.has("stateid") && !accInfo.getString("stateid").isEmpty()) {
                state.setTag(Integer.parseInt(accInfo.getString("stateid")));
                selectedState = accInfo.getString("stateid");
                state.setText(accInfo.getString("statename"));
                setCityDialog();
            }

            addressLine1.setText(accInfo.getString("Addressline1"));
            addressLine2.setText(accInfo.getString("Addressline2"));
            email.setText(accInfo.getString("Email"));
            pincode.setText(accInfo.getString("Zipcode"));
            email.setText(accInfo.getString("Email"));

            /*
            *  inputParams.put("MaritalStatus", marital);
            inputParams.put("Ethnicity", ethnicity);
            */
            marital = accInfo.getString("MaritalStatus");
            ethnicity = accInfo.getString("Ethnicity");


            for (int i = 0; i<masterData.get(0).getObjSubElements().size();i++){
                if (masterData.get(0).getObjSubElements().get(i).getType().equals(marital)){
                    masterData.get(0).getObjSubElements().get(i).setSelected(true);
                }
            }
            for (int i = 0; i<masterData.get(1).getObjSubElements().size();i++){
                if (masterData.get(1).getObjSubElements().get(i).getType().equals(ethnicity)){
                    masterData.get(1).getObjSubElements().get(i).setSelected(true);
                }
            }
            masterAdapter.notifyDataSetChanged();

            occupation.setText(accInfo.getString("Occupation"));
            emer_name.setText(accInfo.getString("EmergencyName"));
            emer_phone.setText(accInfo.getString("EmergencyPhone"));
            referred_by.setText(accInfo.getString("ReferredBy"));
            pob.setText(accInfo.getString("POB"));

        } catch (Exception e) {

        }


    }

    public void setStatesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select State");
        ArrayList<String> optionsAry = new ArrayList<>();
        try {

            JSONArray statesAry = GlobValues.getStateAry();
            for (int i = 0; i < statesAry.length(); i++) {
                stateOptionsAry.add(statesAry.getJSONObject(i).getString("statename"));
                stateOptionsIdsAry.add(statesAry.getJSONObject(i).getString("stateid"));
            }

        } catch (Exception e) {

        }

        final String[] titleList = stateOptionsAry.toArray(new String[optionsAry.size()]);//{"Mediction/Drug", "Food.", "Environment", "Plants", "Cosmetics/Perfume","Flowers","Chemicals","Insect stings/Bites","Others"};
        builder.setItems(titleList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedState = stateOptionsIdsAry.get(which);
                state.setText(stateOptionsAry.get(which));
                state.setTag(stateOptionsIdsAry.get(which));
                city.setText("");
                city.setTag("");
                setCityDialog();

            }
        });
        stateDialog = builder.create();

    }

    public void updateAccount() {

        HashMap<String, Object> inputParams = AppPreferences.getInstance().sendingInputParam();//  new HashMap<String, Object>();
        String webMethodName = Config.LOGIN_VIA_PASSWORD;

//        inputParams.put("patientID","");
        try {
            JSONObject accInfo = AppPreferences.getInstance().getUserInfo();
            inputParams.put("firstname", firName.getText().toString());
            inputParams.put("lastname", lastName.getText().toString());
            inputParams.put("phoneNumber", mobile.getText().toString());
            inputParams.put("dob", dob.getText().toString());
            String genderOption = "";
            if (genderOptions.getSelectedTab() == 0) {
                genderOption = "1";
            } else if (genderOptions.getSelectedTab() == 1) {
                genderOption = "2";
            } else if (genderOptions.getSelectedTab() == 2) {
                genderOption = "0";
            }
            inputParams.put("gender", genderOption);

            inputParams.put("cityid", selectedCity);
            inputParams.put("addr1", addressLine1.getText().toString());
            inputParams.put("addr2", addressLine2.getText().toString());
            inputParams.put("zipcode", pincode.getText().toString());


            inputParams.put("email", email.getText().toString());

            inputParams.put("panno", "");
            inputParams.put("voterid", "");
            inputParams.put("rationid", "");

            inputParams.put("careTaker", "");
            marital = "";
            for (int i = 0; i < masterData.get(0).getObjSubElements().size(); i++) {
                if (masterData.get(0).getObjSubElements().get(i).isSelected()) {
                    marital = masterData.get(0).getObjSubElements().get(i).getType();
                }

            }
            ethnicity = "";
            for (int i = 0; i < masterData.get(1).getObjSubElements().size(); i++) {
                if (masterData.get(1).getObjSubElements().get(i).isSelected()) {
                    ethnicity = masterData.get(1).getObjSubElements().get(i).getType();
                }

            }

            inputParams.put("MaritalStatus", marital);
            inputParams.put("Ethnicity", ethnicity);
            inputParams.put("Occupation", occupation.getText().toString());
            inputParams.put("EmergencyName", emer_name.getText().toString());
            inputParams.put("EmergencyPhone", emer_phone.getText().toString());
            inputParams.put("ReferredBy", referred_by.getText().toString());
            inputParams.put("POB", pob.getText().toString());
            inputParams.put("IsFilling", true);

        } catch (Exception e) {

        }


        inputParams.put("username", mobile.getText().toString());

        inputParams.put("DeviceId", AppPreferences.getInstance().getDeviceToken());
        inputParams.put("iDeviceType", "1");


       // Log.e(TAG, "updateAccount: " + inputParams.toString());
        SoapAPIManager apiManager = new SoapAPIManager(getContext(), inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
               // Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject updatedUserInfo = responseAry.getJSONObject(0);
                        if (updatedUserInfo.has("APIStatus") && Integer.parseInt(updatedUserInfo.getString("APIStatus")) == -1) {
                            if (updatedUserInfo.has("Message") && !updatedUserInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(getContext(), updatedUserInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(getContext(), "Please check again!", false);
                            }
                            return;

                        }

                        JSONObject userInfo = AppPreferences.getInstance().getUserInfo();
                        if (userInfo.has("Lattitude") && userInfo.get("Lattitude") != null && !userInfo.getString("Lattitude").isEmpty() && !userInfo.getString("Lattitude").equals("null")) {
                            updatedUserInfo.put("Lattitude", userInfo.getString("Lattitude"));
                        }
                        if (userInfo.has("Longitude") && userInfo.get("Longitude") != null && !userInfo.getString("Longitude").isEmpty() && !userInfo.getString("Longitude").equals("null")) {
                            updatedUserInfo.put("Longitude", userInfo.getString("Longitude"));
                        }
                        if (userInfo.has("LocationAddress") && userInfo.get("LocationAddress") != null && !userInfo.getString("LocationAddress").isEmpty() && !userInfo.getString("LocationAddress").equals("null")) {
                            updatedUserInfo.put("LocationAddress", userInfo.getString("LocationAddress"));
                        }
                        updatedUserInfo.put("MaritalStatus", marital);
                        updatedUserInfo.put("Ethnicity", ethnicity);
                        updatedUserInfo.put("Occupation", occupation.getText().toString());
                        updatedUserInfo.put("EmergencyName", emer_name.getText().toString());
                        updatedUserInfo.put("EmergencyPhone", emer_phone.getText().toString());
                        updatedUserInfo.put("ReferredBy", referred_by.getText().toString());
                        updatedUserInfo.put("POB", pob.getText().toString());
                        AppPreferences.getInstance().setLoginInfo(updatedUserInfo.toString());
                        //startActivity(new Intent(getContext(), DashboardMapActivity.class));
                    }
                } catch (Exception e) {

                }


            }
        }, false);
        String[] url = {Config.WEB_Services1, Config.UPDATE_PROFILE, "POST"};

        if (Utilities.isNetworkAvailable(getContext())) {
            apiManager.execute(url);
        } else {

        }
    }


    public void setCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select City");
        ArrayList<String> optionsAry = new ArrayList<>();
        cityOptionsAry.clear();
        cityOptionsIdsAry.clear();
        try {

            JSONArray statesAry = GlobValues.getCityAry();
            for (int i = 0; i < statesAry.length(); i++) {
                if (statesAry.getJSONObject(i).getString("stateid").equals(selectedState)) {
                    cityOptionsAry.add(statesAry.getJSONObject(i).getString("cityname"));
                    cityOptionsIdsAry.add(statesAry.getJSONObject(i).getString("cityid"));
                }

            }

        } catch (Exception e) {

        }

        final String[] titleList = cityOptionsAry.toArray(new String[optionsAry.size()]);//{"Mediction/Drug", "Food.", "Environment", "Plants", "Cosmetics/Perfume","Flowers","Chemicals","Insect stings/Bites","Others"};
        builder.setItems(titleList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                city.setText(cityOptionsAry.get(which));
                city.setTag(cityOptionsIdsAry.get(which));
                selectedCity = cityOptionsIdsAry.get(which);
            }
        });
        cityDialog = builder.create();

    }


    public void saveData() {

        updateAccount();

    }

    boolean vaildateUploadData() {
        if (!Utilities.validate(getContext(), firName, getResources().getString(R.string.first_name), false, 2, 50) ||
                !Utilities.validate(getContext(), state, getResources().getString(R.string.state), false, 1, 50) ||
                !Utilities.validate(getContext(), city, getResources().getString(R.string.city), false, 1, 50) ||
                !Utilities.validate(getContext(), addressLine1, getResources().getString(R.string.address_line), false, 7, 50)) {

            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

        Utilities.hideKeyboard(getActivity());
        switch (v.getId()) {

            case R.id.city:
                if (selectedState.isEmpty()) {
                    Utilities.showAlert(getContext(), "Please select State first", true);
                    return;
                }
                cityDialog.show();
                break;
            case R.id.state:
                stateDialog.show();
                break;
            case R.id.dob:
                datepickerdialog.show(getActivity().getFragmentManager(), "DOB");
                break;
            case R.id.updatebutton:
                if (vaildateUploadData()) {
                    updateAccount();
                }

                break;

        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

    }
}