package com.kal.connect.modules.dashboard.tabs.AccountDetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.Fragment;
//import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kal.connect.R;
import com.kal.connect.customLibs.HTTP.GetPost.APICallback;
import com.kal.connect.customLibs.HTTP.GetPost.SoapAPIManager;
import com.kal.connect.customLibs.appCustomization.CustomActivity;
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
import java.util.Map;

import lib.kingja.switchbutton.SwitchMultiButton;

import static com.kal.connect.customLibs.JSONHandler.JSONHandler.toMap;

public class EditAccountActivity extends CustomActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    // MARK : UIElements

    EditText firName, lastName, mobile, weight, height, dob, city, state, addressLine1, addressLine2, pincode, email, password;

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

    Button testCall;

    // MARK : Lifecycle

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account);
        buildUI();
    }

    // MARK : Instance Methods
    private void buildUI() {


//        testCall = (Button) view.findViewById(R.id.test_call_btn);
//        testCall.setOnClickListener(this);
        firName = (EditText) findViewById(R.id.fir_name);
        lastName = (EditText) findViewById(R.id.last_name);
        mobile = (EditText) findViewById(R.id.mobile);
        dob = (EditText) findViewById(R.id.dob);
        weight = (EditText) findViewById(R.id.txtWeight);
        height = (EditText) findViewById(R.id.height);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        addressLine1 = (EditText) findViewById(R.id.address_line1);
        addressLine2 = (EditText) findViewById(R.id.address_line2);
        pincode = (EditText) findViewById(R.id.pincode);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        proceedBtn = (Button) findViewById(R.id.updatebutton);
        genderOptions = (SwitchMultiButton) findViewById(R.id.gender_options);

        String[] d   = {"one","two","three"};




        Calendar now = Calendar.getInstance();
        datepickerdialog = DatePickerDialog.newInstance(
                EditAccountActivity.this,
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

        setupAccInfo();
        setStatesDialog();

        proceedBtn.setOnClickListener(this);

        setHeaderView(R.id.headerView, EditAccountActivity.this, EditAccountActivity.this.getResources().getString(R.string.edit_account_title));
        headerView.showBackOption();

    }

    void setupAccInfo() {
        try {
            JSONObject accInfo = AppPreferences.getInstance().getUserInfo();
            firName.setText(accInfo.getString("FirstName"));
            lastName.setText(accInfo.getString("LastName"));
            mobile.setText(accInfo.getString("ContactNo"));
            String dobString = Utilities.dateRT(accInfo.getString("DOB"));
            dobString = Utilities.changeStringFormat(dobString,"yyyy-mm-dd","dd/mm/yyyy");

            dob.setText(dobString);
            if(!accInfo.getString("Sex").isEmpty()){
                if(Integer.parseInt(accInfo.getString("Sex").trim()) == 1){
                    genderOptions.setSelectedTab(0);
                }else if(Integer.parseInt(accInfo.getString("Sex").trim()) == 2){
                    genderOptions.setSelectedTab(1);
                }else if(Integer.parseInt(accInfo.getString("Sex").trim()) == 2){
                    genderOptions.setSelectedTab(0);
                }
            }

            genderOptions.setSelected(false);


//            city.setText(accInfo.getString("cityname"));
//            state.setText(accInfo.getString("statename"));

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
//            if(accInfo.has("password") && !accInfo.getString("password").isEmpty())
//                password.setText(accInfo.getString("password"));

//            weight.setText(accInfo.getString(""));
//            height.setText(accInfo.getString(""));

            pincode.setText(accInfo.getString("Zipcode"));
            email.setText(accInfo.getString("Email"));
        } catch (Exception e) {

        }


    }

    public void setStatesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditAccountActivity.this);
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


    public void setCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditAccountActivity.this);
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

    public Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
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
            if(genderOptions.getSelectedTab() == 0){
                genderOption = "1";
            }else if(genderOptions.getSelectedTab() == 1){
                genderOption = "2";
            }else if(genderOptions.getSelectedTab() == 2){
                genderOption = "0";
            }
            inputParams.put("gender", genderOption);

            inputParams.put("cityid", selectedCity);
            inputParams.put("addr1", addressLine1.getText().toString());
            inputParams.put("addr2", addressLine2.getText().toString());
            inputParams.put("zipcode", pincode.getText().toString());

//            inputParams.put("cityid", "106");
//            inputParams.put("addr1", "Default update address1");
//            inputParams.put("addr2", "Default update address2");
//            inputParams.put("zipcode", "110005");



            inputParams.put("email", email.getText().toString());

            inputParams.put("panno", "");
            inputParams.put("voterid", "");
            inputParams.put("rationid", "");

            inputParams.put("careTaker", "");
            inputParams.put("maritalStatus", "");

            //            inputParams.put("password", password.getText().toString());
        } catch (Exception e) {

        }


        inputParams.put("username", mobile.getText().toString());

        inputParams.put("DeviceId", AppPreferences.getInstance().getDeviceToken());
        inputParams.put("iDeviceType", "1");


        SoapAPIManager apiManager = new SoapAPIManager(EditAccountActivity.this, inputParams, new APICallback() {
            @Override
            public void responseCallback(Context context, String response) throws JSONException {
                Log.e("***response***", response);

                try {
                    JSONArray responseAry = new JSONArray(response);
                    if (responseAry.length() > 0) {
                        JSONObject updatedUserInfo = responseAry.getJSONObject(0);
                        if (updatedUserInfo.has("APIStatus") && Integer.parseInt(updatedUserInfo.getString("APIStatus")) == -1) {
                            if (updatedUserInfo.has("Message") && !updatedUserInfo.getString("Message").isEmpty()) {
                                Utilities.showAlert(EditAccountActivity.this, updatedUserInfo.getString("Message"), false);
                            } else {
                                Utilities.showAlert(EditAccountActivity.this, "Please check again!", false);
                            }
                            return;

                        }


//                        userInfo.put("Lattitude",inputParams.get("Lattitude").toString());
//                        userInfo.put("Longitude",inputParams.get("Longitude").toString());
//                        userInfo.put("LocationAddress",inputParams.get("LocationAddress").toString());
                        JSONObject userInfo = AppPreferences.getInstance().getUserInfo();
                        if(userInfo.has("Lattitude") && userInfo.get("Lattitude") != null && !userInfo.getString("Lattitude").isEmpty()  && !userInfo.getString("Lattitude").equals("null")){
                            updatedUserInfo.put("Lattitude",userInfo.getString("Lattitude"));
                        }
                        if(userInfo.has("Longitude") && userInfo.get("Longitude") != null && !userInfo.getString("Longitude").isEmpty()  && !userInfo.getString("Longitude").equals("null"))
                        {
                            updatedUserInfo.put("Longitude",userInfo.getString("Longitude"));
                        }
                        if(userInfo.has("LocationAddress") && userInfo.get("LocationAddress") != null && !userInfo.getString("LocationAddress").isEmpty()  && !userInfo.getString("LocationAddress").equals("null"))
                        {
                            updatedUserInfo.put("LocationAddress",userInfo.getString("LocationAddress"));
                        }
                        AppPreferences.getInstance().setLoginInfo(updatedUserInfo.toString());
                        startActivity(new Intent(EditAccountActivity.this, DashboardMapActivity.class));
                    }
                } catch (Exception e) {

                }


            }
        }, true);
        String[] url = {Config.WEB_Services1, Config.UPDATE_PROFILE, "POST"};

        if (Utilities.isNetworkAvailable(EditAccountActivity.this)) {
            apiManager.execute(url);
        } else {

        }
    }


    // MARK : UIActions
    @Override
    public void onClick(View v) {

        Utilities.hideKeyboard(EditAccountActivity.this);
        switch (v.getId()) {
//            case R.id.test_call_btn:
//                Intent intent = new Intent(getContext(), VideoCaller.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                String TOKEN = "T1==cGFydG5lcl9pZD00NjI2MDIwMiZzaWc9ZDg1ZGY0MDU1YWM0ZTg4ZTQyZjk5YTA4YjU4MTMxMTBiODU4ZGViNTpzZXNzaW9uX2lkPTJfTVg0ME5qSTJNREl3TW41LU1UVTBPRGcyTlRnNE5UWXpOWDVNTjNKbVRWaEJPR1JJU0dSc1FsZHBVSGs1WjFoNmVXcC1mZyZjcmVhdGVfdGltZT0xNTQ4ODY1OTEzJm5vbmNlPTAuMTgzMDY1MzQ5OTUxMTQ2MzImcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTU1MTQ1NzkxMiZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
//                String SESSION = "2_MX40NjI2MDIwMn5-MTU0ODg2NTg4NTYzNX5MN3JmTVhBOGRISGRsQldpUHk5Z1h6eWp-fg";
//
//                intent.putExtra("SESSION_ID",SESSION);
//                intent.putExtra("TOKEN",TOKEN);
//
//                intent.putExtra("CALER_NAME","Test Call");
//                intent.putExtra("CALL_TYPE",1);
//                startActivity(intent);
//                break;
            case R.id.city:
                if (selectedState.isEmpty()) {
                    Utilities.showAlert(EditAccountActivity.this, "Please select State first", true);
                    return;
                }
                cityDialog.show();
                break;
            case R.id.state:
                stateDialog.show();
                break;
            case R.id.dob:
                datepickerdialog.show(getFragmentManager(),"DOB");
                break;
            case R.id.updatebutton:
                if(vaildateUploadData()){
                    updateAccount();
                }

                break;

        }

    }

    boolean vaildateUploadData(){
        if(!Utilities.validate(this, firName, getResources().getString(R.string.first_name), false, 2, 50) ||
            !Utilities.validate(this, state, getResources().getString(R.string.state), false, 1, 50) ||
            !Utilities.validate(this, city, getResources().getString(R.string.city), false, 1, 50) ||
                !Utilities.validate(this, addressLine1, getResources().getString(R.string.address_line), false, 7, 50)) {

            return false;
        }
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dob.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);

    }
}